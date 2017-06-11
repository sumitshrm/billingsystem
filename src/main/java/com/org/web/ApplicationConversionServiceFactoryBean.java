package com.org.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import com.org.entity.Aggreement;
import com.org.entity.Company;
import com.org.entity.ItemName;
import com.org.entity.Labour;
import com.org.entity.LabourSupplier;
import com.org.entity.Supplier;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}
	
	public Converter<Aggreement, String> getAggreementToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Aggreement, java.lang.String>() {
            public String convert(Aggreement aggreement) {
                return new StringBuilder().append(aggreement.getAggreementNum()).append(" : ").append(aggreement.getNameOfWork()).toString();
            }
        };
    }
	
	public Converter<Company, String> getCompanyToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Company, java.lang.String>() {
            public String convert(Company company) {
                return new StringBuilder().append(company.getName()).toString();
            }
        };
    }
	
	 public Converter<Labour, String> getLabourToStringConverter() {
	        return new org.springframework.core.convert.converter.Converter<com.org.entity.Labour, java.lang.String>() {
	            public String convert(Labour labour) {
	                return new StringBuilder().append(labour.getName()).toString();
	            }
	        };
	    }
	  
	 public Converter<LabourSupplier, String> getLabourSupplierToStringConverter() {
	        return new org.springframework.core.convert.converter.Converter<com.org.entity.LabourSupplier, java.lang.String>() {
	            public String convert(LabourSupplier labourSupplier) {
	                return new StringBuilder().append(labourSupplier.getName()).toString();
	            }
	        };
	    }
	 
	 public Converter<ItemName, String> getItemNameToStringConverter() {
	        return new org.springframework.core.convert.converter.Converter<com.org.entity.ItemName, java.lang.String>() {
	            public String convert(ItemName itemName) {
	                return new StringBuilder().append(itemName.getName()).toString();
	            }
	        };
	    }
	  
	 public Converter<Supplier, String> getSupplierToStringConverter() {
	        return new org.springframework.core.convert.converter.Converter<com.org.entity.Supplier, java.lang.String>() {
	            public String convert(Supplier supplier) {
	                return new StringBuilder().append(supplier.getName()).toString();
	            }
	        };
	    }
}
