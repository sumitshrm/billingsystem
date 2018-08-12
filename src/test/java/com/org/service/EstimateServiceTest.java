package com.org.service;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.org.entity.Estimate;
import com.org.service.blobstore.FileStorageService;
import com.org.util.FileStorageProperties;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class EstimateServiceTest {
	
	@InjectMocks
	private EstimateService estimateService;
	
	@Mock
	private FileStorageService fileStorageService;
	
	ObjectMapper mapper= new ObjectMapper();
	
	@Test
	public void testWriteExcelDocument() throws Exception {
		Estimate estimate = getEstimate();
		InputStream input = getExcelInputStream();
		when(fileStorageService.doGet(FileStorageProperties.TEMPLATE_FILE)).thenReturn(input);
		getExcelInputStream();
		estimateService.createEstimate(estimate);

	}
	
	private Estimate getEstimate() throws IOException {
		/*String content = new String(Files.readAllBytes(Paths.get("/estimate/estimate.json")));
		System.out.println(content);*/
		String json = IOUtils.toString(
			      this.getClass().getResourceAsStream("/estimates/estimate.json"),
			      "UTF-8"
			    );
		System.out.println(json);
		Estimate estimate = mapper.readValue(json, Estimate.class);
		System.out.println(estimate);
		return estimate;
	}
	
	private InputStream getExcelInputStream() throws FileNotFoundException {
		/*FileInputStream excelFile = new FileInputStream(new File("/ESTIMATE.xlsx"));
		return excelFile;*/
		
		return this.getClass().getResourceAsStream("/ESTIMATE.xlsx");
	}
	
	private void saveExcel(String filename, InputStream is) throws IOException {
		File outputFile = new File("test.xlsx");
		outputFile.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		copy(is, outputStream);
	}
	
	private void copy(InputStream input, OutputStream output) throws IOException {
		
		int BUFFER_SIZE = 2 * 1024 * 1024;
	    try {
	      byte[] buffer = new byte[BUFFER_SIZE];
	      int bytesRead = input.read(buffer);
	      while (bytesRead != -1) {
	        output.write(buffer, 0, bytesRead);
	        bytesRead = input.read(buffer);
	      }
	    } finally {
	      input.close();
	      output.close();
	    }
	  }

}
