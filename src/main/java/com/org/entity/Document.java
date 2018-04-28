package com.org.entity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Document {
 
    @Size(max = 100)
    private String name;

    /**
     */
    @Size(max = 500)
    private String description;

    private String filename;
    
    @Transient
    private byte[] content;
    
    @NotNull
    @Size(max = 200)
    private String url ;
    
    @Transient
    private transient XSSFWorkbook workbook;
    
    @Transient
    private File excelFile;
    
    
    public XSSFWorkbook getWorkbook() {
		if(this.workbook==null){
			try {
				System.out.println("OPCPackage.open(getUrl()):start");
				OPCPackage pkg = OPCPackage.open(getUrl());
				System.out.println("OPCPackage.open(getUrl()):finish");
			    this.workbook = new XSSFWorkbook(pkg);
			    System.out.println("new XSSFWorkbook(pkg):finish");

			} catch (IOException | InvalidFormatException e) {
				e.printStackTrace();
			}
		}
		return this.workbook;
	}
	
	public File getExcelFile(){
		if(this.excelFile==null){
			this.excelFile =  new File(getUrl());
		}
		return this.excelFile;
	}
	
	public void save(){
		
		FileOutputStream out;
		File tempFile = new File(getUrl()+"_Temp.xlsx");
		try {
			out = new FileOutputStream(tempFile);
			this.getWorkbook().write(out);
			out.close();
			workbook.close();
			tempFile.delete();
			System.out.println("closed workbook");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	@Override
	public String toString() {
		return filename;
	}
	
	
}
