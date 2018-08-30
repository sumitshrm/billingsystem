package com.org.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;

import com.org.util.FileStorageProperties;

@RunWith(MockitoJUnitRunner.class)
public class WordTemplateTest {
	
	@Test
	public void testWork() throws IOException {
		//Blank Document
		  InputStream is = getWordInputStream();
	      XWPFDocument document = new XWPFDocument(is); 
			
	      //Write the Document in file system
	      FileOutputStream out = new FileOutputStream( new File("createdocument.docx"));
	      document.getProperties().getCustomProperties().addProperty("test" , "testName");
	      //document.getProperties().getCustomProperties().addProperty("test" , "testName2");
	      document.getProperties().getCustomProperties().getProperty("test").setLpwstr("ohh");
	      System.out.println("get prop " + document.getProperties().getCustomProperties().getProperty("test").getLpwstr());
	      document.write(out);
	      //doc_id
	      //url
	      //xl_gateway_url
	      out.close();
	      System.out.println("createdocument.docx written successully");

	}

	private InputStream getWordInputStream() throws FileNotFoundException {
		/*FileInputStream excelFile = new FileInputStream(new File("/ESTIMATE.xlsx"));
		return excelFile;*/
		
		return this.getClass().getResourceAsStream("/DOC_TEMPLATE.docm");
	}
}
