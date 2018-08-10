package com.org.web;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import com.org.entity.Document;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.report.service.ItemsGeneratorService;
import com.org.service.DocumentService;
import com.org.service.blobstore.FileStorageService;

@RequestMapping("/documents")
@Controller
@RooWebScaffold(path = "documents", formBackingObject = Document.class, update = false)
public class DocumentController {

    @Autowired
    ItemsGeneratorService itemService;

    @Autowired
    private FileStorageService fileStorageService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "savedoc", method = RequestMethod.POST)
    public String createdoc(@Valid Document document, BindingResult result, Model model, @RequestParam("content") MultipartFile content, HttpServletRequest request) {
        System.out.println(document);
        System.out.println("Document: ");
        System.out.println("Name: " + content.getOriginalFilename());
        System.out.println("File: " + content.getName());
        System.out.println("Type: " + content.getContentType());
        System.out.println("Size: " + content.getSize());
        document.persist();
        document.setUrl(request.getContextPath() + "/documents/showdoc/" + document.getId());
        document.persist();
        return "redirect:/documents?page=1&amp;size=10" + encodeUrlPathSegment(document.getId().toString(), request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        Document doc = Document.findDocument(id);
        doc.setUrl("./showdoc/" + id);
        model.addAttribute("document", Document.findDocument(id));
        model.addAttribute("itemId", id);
        return "documents/show";
    }

    @RequestMapping(value = "/showdoc/{id}", method = RequestMethod.GET)
    public String showdoc(@PathVariable("id") Long id, HttpServletResponse response, Model model) {
        MeasurementSheet msheet = MeasurementSheet.findMeasurementSheet(id);
        System.out.println("downloading....");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\"" + ExcelUtill.getFileNameWithTimeStamp(msheet.getDocument().getFilename()) + "\"");
            OutputStream out = response.getOutputStream();
            InputStream inputStream = fileStorageService.doGet(msheet.getStorageFileName());
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            itemService.writeItems(msheet.getAggreement().getItems(), wb, msheet, true);
            wb.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //msheet.getDocument().close();
        }
        return null;
    }
}
