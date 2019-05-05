package com.org.web;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.management.Query;
import javax.naming.Context;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.org.constants.ManagedDocumentType;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Estimate;
import com.org.entity.EstimateShared;
import com.org.entity.Item;
import com.org.entity.ItemsXMLData;
import com.org.entity.ItemsXml;
import com.org.entity.ManagedDocument;
import com.org.entity.MeasurementSheet;
import com.org.service.blobstore.FileStorageService;
import com.org.util.QueryUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/aggreements")
@Controller
@RooWebScaffold(path = "aggreements", formBackingObject = Aggreement.class)
public class AggreementController {

    @Autowired
    private FileStorageService fileStorageService;
    
    @RequestMapping(params = "redirect", produces = "text/html", method=RequestMethod.GET)
    public String createFromBillform(@RequestParam(value = "redirect", required = true)String redirect,Model uiModel) {
        populateEditForm(uiModel, new Aggreement());
        uiModel.addAttribute("redirect",redirect);
        return "aggreements/create";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogUser user = LogUser.findLogUsersByUsernameEquals(username).getSingleResult();
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(user, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Aggreement.countAggreements() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("aggreements", Aggreement.findAggreementsByLogUser(user, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "aggreements/list";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        Aggreement aggreement = QueryUtil.getUniqueResult(Aggreement.findAggreementsByIdAndLogUser(id, getCurrentUser()));
        uiModel.addAttribute("aggreement", aggreement);
        uiModel.addAttribute("manageddocuments", ManagedDocument.findManagedDocumentsByAggreement(aggreement).getResultList());
        uiModel.addAttribute("itemId", id);
        return "aggreements/show";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Aggreement aggreement = Aggreement.findAggreement(id);
        Set<MeasurementSheet> msheets = aggreement.getMeasurementSheets();
        if (msheets != null) {
            for (MeasurementSheet msheet : msheets) {
                boolean fileDeleted = fileStorageService.delete(msheet.getStorageFileName());
                if (!fileDeleted) {
                    // TODO:add to orphan files database;
                }
            }
        }
        aggreement.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/aggreements";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@RequestParam(value = "redirect", required = false) String redirect, @Valid Aggreement aggreement, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        System.out.println("post called billformn"+redirect);
        LogUser user = LogUser.getCurrentUser();
    	if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, aggreement);
            return "aggreements/create";
        }
        uiModel.asMap().clear();
        aggreement.persist();
        if(redirect!=null) {
        	return "redirect:"+redirect;
        }
        
        //Craete folder for aggreement
        try {
        	ManagedDocument myDoc = new ManagedDocument();
            myDoc.setType(ManagedDocumentType.FOLDER);
            myDoc.setDescription(aggreement.getAggreementNum());
            ManagedDocument root = ManagedDocument.findManagedDocumentsByLogUserAndType(user, ManagedDocumentType.AGG_FOLDER).getSingleResult();
            myDoc.setParent(root);
            myDoc.merge();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return "redirect:/items/aggreement/" + encodeUrlPathSegment(aggreement.getId().toString(), httpServletRequest);
    }

    private LogUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogUser user = LogUser.findLogUsersByUsernameEquals(username).getSingleResult();
        return user;
    }
    
    @RequestMapping(value = "/{agg}/schedule", method = RequestMethod.GET, produces = "text/html")
    public String createSchedule(@PathVariable("agg") Long agg, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
        Aggreement aggreement = Aggreement.findAggreementsByIdAndLogUser(agg, user).getSingleResult();
        
        try {
        	InputStream inputStream = fileStorageService.doGet("ManagedDocuments/MANAGED_DOCUMENTS_NA_110_DSR_2016.xml");
        	List<ItemsXMLData> entries = ((ItemsXml)JAXBContext.newInstance(ItemsXml.class).createUnmarshaller().unmarshal(inputStream)).getEntries();
        	ObjectMapper mapper = new ObjectMapper();
        	String dsrItemsJson = mapper.writeValueAsString(entries);
        	uiModel.addAttribute("dsrItemsJson", dsrItemsJson);
        	uiModel.addAttribute("dsrItems", entries);
        	//System.out.println(entries);
        } catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally {
			//test
		}
        //List<Entry> entries = (ItemsXml)(JAXBContext.newInstance(ItemsXml.class).createUnmarshaller().unmarshal(inputStream)).getEntries();
        uiModel.addAttribute("aggreement", aggreement);
        uiModel.addAttribute("items", Item.findItemsByAggreementAndLogUserAndFullRateIsNotNull(aggreement, user, "id", "ASC").getResultList());
        return "aggreements/schedule";
    }
    
    @RequestMapping(value="/{agg}/schedule/saveitem", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createStatement(@PathVariable("agg") Long agg, @RequestBody ItemsXml itemsXml, HttpServletResponse httpServletResponse) throws Exception {
	 	System.out.println(itemsXml);
	 	Aggreement aggreement = Aggreement.findAggreement(agg);
	 	Item parentItem = null;
	 	for(ItemsXMLData item : itemsXml.getEntries()) {
	 		Item itemObject = new Item();
	 		try {
	 			//if item already exist then do not add simply make it parent of next item
	 			parentItem = Item.findItemsByItemNumberAndAggreement(item.getItemNumber(), aggreement).getSingleResult();
	 			if(parentItem.getFullRate()!=null) {
	 				return new ResponseEntity<String>("Item already exist",HttpStatus.INTERNAL_SERVER_ERROR);
	 			}
	 			
	 		}catch (EmptyResultDataAccessException e) {
	 			itemObject.setItemNumber(item.getItemNumber());
		 		itemObject.setParentItem(parentItem);
		 		itemObject.setAggreement(aggreement);
		 		itemObject.setDescription(item.getDescription());
		 		itemObject.setUnit(item.getUnit());
		 		itemObject.setFullRate(item.getFullRate());
		 		try {
		 			itemObject.persist();
				} catch (Exception ex) {
					e.printStackTrace();
					return new ResponseEntity<String>("Some error occured",HttpStatus.INTERNAL_SERVER_ERROR);
				}
		 		
		 		parentItem = itemObject;
		 		System.out.println("saved item " + item);
			}catch (Exception e) {
				e.printStackTrace();
			}
	 	}
    	return new ResponseEntity<String>("item saved successfully",HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/saveextraitem", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createExtraItem(@PathVariable("agg") Long agg,@RequestBody Item item, HttpServletResponse httpServletResponse) throws Exception {
	 	System.out.println(item);
	 	Aggreement aggreement = Aggreement.findAggreement(agg);
	 	try {
 			//if item already exist then do not add simply make it parent of next item
 			Item.findItemsByItemNumberAndAggreement(item.getItemNumber(), aggreement).getSingleResult();
 			return new ResponseEntity<String>("Item already exist",HttpStatus.INTERNAL_SERVER_ERROR);
 		}catch(EmptyResultDataAccessException e) {
 			item.setAggreement(aggreement);
 			item.setIsExtraItem(true);
 			try {
 				item.persist();
 			}catch (Exception ex) {
				ex.printStackTrace();
			}
 			
 		}
    	return new ResponseEntity<String>("item added successfully",HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/deleteitem", method = RequestMethod.DELETE)
    public ResponseEntity createExtraItem(@PathVariable("agg") Long agg,@RequestParam(value = "itemNumber", required = false) String itemNumber, HttpServletResponse httpServletResponse) throws Exception {
    	Aggreement aggreement = Aggreement.findAggreement(agg);
    	try {
    		Item item = Item.findItemsByItemNumberAndAggreement(itemNumber, aggreement).getSingleResult();
    		do {
    			try {
    				item.remove();
    				item=item.getParentItem();
				} catch (Exception e) {
					break;
				}
				
			} while (item!=null && item.getSubItems().size()==0);
    		
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("item not found",HttpStatus.INTERNAL_SERVER_ERROR);
		}	
    	
    	
    	return new ResponseEntity<String>("item deleted successfully",HttpStatus.OK);
    }
}
