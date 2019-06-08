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
import com.org.entity.Document;
import com.org.entity.Estimate;
import com.org.entity.EstimateShared;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.ItemsXMLData;
import com.org.entity.ItemsXml;
import com.org.entity.ManagedDocument;
import com.org.entity.MeasurementSheet;
import com.org.service.DocumentService;
import com.org.service.blobstore.FileStorageService;
import com.org.util.FileStorageProperties;
import com.org.util.QueryUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    
    @Autowired
    private DocumentService documentService;
    
    
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
        
        return "redirect:/aggreements/" + encodeUrlPathSegment(aggreement.getId().toString(), httpServletRequest)+"/schedule";
    }

    private LogUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LogUser user = LogUser.findLogUsersByUsernameEquals(username).getSingleResult();
        return user;
    }
    
    @RequestMapping(value = "/{agg}/schedule", method = RequestMethod.GET, produces = "text/html")
    public String createSchedule(@PathVariable("agg") Long agg, @RequestParam(value = "msheetid", required = false) Long msheetid,@RequestParam(value = "dsr", required = false) Integer dsr, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
        Aggreement aggreement = Aggreement.findAggreementsByIdAndLogUser(agg, user).getSingleResult();
        //List<Entry> entries = (ItemsXml)(JAXBContext.newInstance(ItemsXml.class).createUnmarshaller().unmarshal(inputStream)).getEntries();
        uiModel.addAttribute("aggreement", aggreement);
    	Integer dsrfile=dsr==null?2018:dsr;
        uiModel.addAttribute("dsr", dsrfile);
        if(msheetid!=null) {
        	List<Item> items = Item.findItemsByAggreementAndMeasurementSheetIdAndFullRateIsNotNull(aggreement, msheetid, "id", "ASC").getResultList();
        	uiModel.addAttribute("items", items);
        	uiModel.addAttribute("msheetid", msheetid);
        }else {
            List<Item> items = Item.findItemsByAggreementAndLogUserAndFullRateIsNotNullAndIsExtraItem(aggreement, user,false, "id", "ASC").getResultList();
            uiModel.addAttribute("items", items);
        	
        }
        return "aggreements/schedule";
    }
    
    @RequestMapping(value="/schedule/v1/{dsr}")
    ResponseEntity<List<ItemsXMLData>> hello(Long msheetid,@PathVariable(value = "dsr") Integer dsr) throws IOException, JAXBException {
    	String filename=dsr==null||dsr==2018?FileStorageProperties.DSR_FILE_2018:FileStorageProperties.DSR_FILE_2016;
    	InputStream inputStream = fileStorageService.doGet(filename);
    	List<ItemsXMLData> entries = ((ItemsXml)JAXBContext.newInstance(ItemsXml.class).createUnmarshaller().unmarshal(inputStream)).getEntries();
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/saveitem", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveItem(@PathVariable("agg") Long agg, @RequestBody ItemsXml itemsXml,@RequestParam(value = "msheetid", required = false) Long msheetid, HttpServletResponse httpServletResponse) throws Exception {
	 	System.out.println(itemsXml);
	 	Aggreement aggreement = Aggreement.findAggreement(agg);
	 	LogUser user = LogUser.getCurrentUser();
	 	Item parentItem = null;
	 	boolean addParentItem = false;
	 	int x = 0;
	 	//validate if item already exist
	 	String finalitemnumber = itemsXml.getEntries().get(itemsXml.getEntries().size()-1).getItemNumber();
	 	if(Item.countFindItemsByDrsCodeAndAggreement(finalitemnumber, aggreement)>0) {
			return new ResponseEntity<String>("Item already exist",HttpStatus.INTERNAL_SERVER_ERROR);
	 	}
	 	Item itemForAbstract = null;
	 	
	 	for(ItemsXMLData item : itemsXml.getEntries()) {
	 		x++;
	 		Item itemObject = new Item();
	 		try {
	 			//if item already exist then do not add simply make it parent of next item
	 			if(x<=1) { //for the first item. check if this is the latest item added.
	 				parentItem=Item.findLatestItemByAggreementAndParentItemIsNull(aggreement).getSingleResult();
	 				if(!parentItem.getDrsCode().equals(item.getItemNumber())) {
	 					parentItem=null;
	 					addParentItem=true;
	 					throw new EmptyResultDataAccessException(1);
	 				}
	 					
	 			}
	 			else {
	 				parentItem = Item.findItemsByDrsCodeAndAggreement(item.getItemNumber(), aggreement).getSingleResult();
		 			if(parentItem.getFullRate()!=null) {
		 				return new ResponseEntity<String>("Item already exist",HttpStatus.INTERNAL_SERVER_ERROR);
		 			}
	 			}
	 			
	 		}catch (EmptyResultDataAccessException e) {
	 			itemObject.setItemNumber(item.getItemNumber());
		 		itemObject.setParentItem(parentItem);
		 		itemObject.setAggreement(aggreement);
		 		itemObject.setDescription(item.getDescription());
		 		itemObject.setUnit(item.getUnit());
		 		itemObject.setFullRate(item.getFullRate());
		 		itemObject.setDrsCode(item.getItemNumber());
		 		itemObject.setPartRate(item.getFullRate());
		 		itemObject.setQuantity(1d);
		 		
		 		try {
		 			if(msheetid!=null) {
			 			itemObject.setIsExtraItem(true);
			 			itemObject.setMeasurementSheetId(msheetid);
			 			itemForAbstract = itemObject; // abstract will be created for the last occurence of extra item
			 		}
		 			itemObject.persist();
		 			finalitemnumber = itemObject.getDrsCode();
				} catch (Exception ex) {
					ex.printStackTrace();
					return new ResponseEntity<String>("Some error occured",HttpStatus.INTERNAL_SERVER_ERROR);
				}
		 		
		 		parentItem = itemObject;
		 		System.out.println("saved item " + item);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>("Some error occured",HttpStatus.INTERNAL_SERVER_ERROR);
			}
	 	}
	 	
    	return new ResponseEntity<String>(finalitemnumber,HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/saveextraitem", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createExtraItem(@PathVariable("agg") Long agg,@RequestBody Item item,@RequestParam(value = "msheetid", required = false) Long msheetid, HttpServletResponse httpServletResponse) throws Exception {
	 	System.out.println(item);
	 	Aggreement aggreement = Aggreement.findAggreement(agg);
	 	try {
 			//if item already exist then do not add simply make it parent of next item
 			Item.findItemsByDrsCodeAndAggreement(item.getItemNumber(), aggreement).getSingleResult();
 			return new ResponseEntity<String>("Item already exist",HttpStatus.INTERNAL_SERVER_ERROR);
 		}catch(EmptyResultDataAccessException e) {
 			item.setAggreement(aggreement);
 			item.setDrsCode(item.getItemNumber());
 			item.setPartRate(item.getFullRate());
 			if(item.getQuantity()==0) {
 				item.setQuantity(null);
 				item.setFullRate(null);
 			}
 			try {
 				//find parent item
 				int pos = item.getItemNumber().lastIndexOf(".");
 				if(pos>=0) {
 					String parentItemNum = item.getItemNumber().substring(0, pos);
 					try {
 	 					Item parentItem = Item.findItemsByItemNumberAndAggreement(parentItemNum, aggreement).getSingleResult();
 	 					item.setParentItem(parentItem);
 					}catch (EmptyResultDataAccessException ex) {
 						return new ResponseEntity<String>("Invalid item number. parent item '"+parentItemNum+"' missing",HttpStatus.INTERNAL_SERVER_ERROR);
					}catch (Exception ey) {
						return new ResponseEntity<String>("Some error occurred.",HttpStatus.INTERNAL_SERVER_ERROR);
					}
 				}
 				if(msheetid!=null) {
 					item.setIsExtraItem(true);
 					item.setMeasurementSheetId(msheetid);
 				}
 				
 				item.persist();
 			}catch (Exception ex) {
				ex.printStackTrace();
			}
 			
 		}
	 	if(item.getQuantity()==null) {
	 		return new ResponseEntity<String>("main item added successfully. please add subitems now",HttpStatus.OK);
	 	}
	 	
    	return new ResponseEntity<String>("item added successfully",HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/deleteitem", method = RequestMethod.DELETE)
    public ResponseEntity deleteExtraItem(@PathVariable("agg") Long agg,@RequestParam(value = "itemNumber", required = false) String dsrcode, HttpServletResponse httpServletResponse) throws Exception {
    	Aggreement aggreement = Aggreement.findAggreement(agg);
    	try {
    		Item item = Item.findItemsByDrsCodeAndAggreement(dsrcode, aggreement).getSingleResult();
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
		}	catch(Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<String>("item not found",HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    	
    	return new ResponseEntity<String>("item deleted successfully",HttpStatus.OK);
    }
    
    @RequestMapping(value="/{agg}/schedule/review")
    public String reviewAndUpdateSchedule(@PathVariable("agg")Long agg, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
    	Aggreement aggreement = Aggreement.findAggreementsByIdAndLogUser(agg, user).getSingleResult();
    	List<Item> parentItems = Item.findItemsByAggreementAndLogUserAndParentItemIsNullAndIsExtraItem(aggreement, user,false, "id", "ASC").getResultList();
    	Integer serialnum=1;
    	for(Item item : parentItems) {
    		if(!item.getItemNumber().equals(serialnum.toString())) {
    			item.setItemNumber(serialnum.toString());
    			//if(item.isIsExtraItem()) {item.setDrsCode(item.getItemNumber());}
        		updateItemNumber(item.getSubItems(), 1);
        		item.persist();
    		}
    		serialnum++;
    	}
    	List<Item> updatedItems = Item.findItemsByAggreementAndLogUserAndFullRateIsNotNullAndIsExtraItem(aggreement, user, false, "id", "ASC").getResultList();
    	uiModel.addAttribute("items", updatedItems);
    	uiModel.addAttribute("aggreement", aggreement);
    	return "aggreements/reviewschedule";
    }
    
    @RequestMapping(value="/{agg}/schedule/reviewextraitem/{msheetid}")
    public String reviewAndUpdateExtraItem(@PathVariable("agg")Long agg,@PathVariable("msheetid")Long msheetid, Model uiModel) {
    	LogUser user = LogUser.getCurrentUser();
    	Aggreement aggreement = Aggreement.findAggreementsByIdAndLogUser(agg, user).getSingleResult();
    	List<Item> parentItems = Item.findItemsByAggreementAndLogUserAndParentItemIsNullAndIsExtraItem(aggreement, user,true, "id", "ASC").getResultList();
    	Integer serialnum=1;
    	for(Item item : parentItems) {
    		if(!item.getItemNumber().equals("EI"+serialnum.toString())) {
    			item.setItemNumber("EI"+serialnum.toString());
    			//if(item.isIsExtraItem()) {item.setDrsCode(item.getItemNumber());}
        		updateItemNumber(item.getSubItems(), 1);
        		item.persist();
    		}
    		serialnum++;
    	}
    	MeasurementSheet measurementSheet = MeasurementSheet.findMeasurementSheetsByIdAndLogUser(msheetid, user).getSingleResult();
    	Document defaultDoc;
        try {
            defaultDoc = documentService.createDefaultDocument(measurementSheet);
            defaultDoc.persist();
            measurementSheet.setDocument(defaultDoc);
            documentService.generateReportManualy(measurementSheet);
            documentService.generateReport(measurementSheet);
            measurementSheet.persist();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(); 
        }
    	List<Item> updatedItems = Item.findItemsByAggreementAndLogUserAndFullRateIsNotNull(aggreement, user	, "id", "ASC").getResultList();
    	uiModel.addAttribute("items", updatedItems);
    	uiModel.addAttribute("aggreement", aggreement);
    	return "redirect:/measurementsheets/" + msheetid;
    }
    
    @RequestMapping(value="/{agg}/schedule/updatequantity")
    public ResponseEntity saveItemQuantity(@PathVariable("agg") Long agg,@RequestParam(value = "id", required = true) long id,@RequestParam(value = "value", required = false) Double quantity) {
    	LogUser user = LogUser.getCurrentUser();
    	try {
        	Aggreement aggreement = Aggreement.findAggreementsByIdAndLogUser(agg, user).getSingleResult();
        	Item item = Item.findItem(id);
        	item.setQuantity(quantity);
        	item.persist();
        	return new ResponseEntity<String>("quantity updated successfully",HttpStatus.OK);
    	}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(quantity.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
    
    private void updateItemNumber(List<Item> items, Integer serialnum) {
    	for(Item item : items) {
    		item.setItemNumber(item.getParentItem().getItemNumber()+"."+serialnum);
    		//if(item.isIsExtraItem()) {item.setDrsCode(item.getItemNumber());}
    		if(item.getSubItems().size()>0) { 
    			updateItemNumber(item.getSubItems(), serialnum);
    		}
    		serialnum++;
    		item.persist();
    		
    	}
    }
    
}
