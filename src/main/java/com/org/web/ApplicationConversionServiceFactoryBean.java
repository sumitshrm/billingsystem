package com.org.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;
import com.org.domain.Config;
import com.org.domain.LogUser;
import com.org.domain.LogUserRole;
import com.org.entity.Aggreement;
import com.org.entity.Company;
import com.org.entity.Document;
import com.org.entity.Estimate;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.ItemName;
import com.org.entity.Labour;
import com.org.entity.LabourEntry;
import com.org.entity.LabourSupplier;
import com.org.entity.ManagedDocument;
import com.org.entity.MaterialEntry;
import com.org.entity.MeasurementSheet;
import com.org.entity.Supplier;
import com.org.entity.Template;

@Configurable
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

	public Converter<Config, String> getConfigToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.Config, java.lang.String>() {
            public String convert(Config config) {
                return new StringBuilder().append(config.getCellName()).append(' ').append(config.getValue()).toString();
            }
        };
    }

	public Converter<Long, Config> getIdToConfigConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.Config>() {
            public com.org.domain.Config convert(java.lang.Long id) {
                return Config.findConfig(id);
            }
        };
    }

	public Converter<String, Config> getStringToConfigConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.Config>() {
            public com.org.domain.Config convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Config.class);
            }
        };
    }

	public Converter<LogUser, String> getLogUserToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.LogUser, java.lang.String>() {
            public String convert(LogUser logUser) {
                return new StringBuilder().append(logUser.getUsername()).append(' ').append(logUser.getFullName()).append(' ').append(logUser.getPassword()).append(' ').append(logUser.getEmailAddress()).toString();
            }
        };
    }

	public Converter<Long, LogUser> getIdToLogUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.LogUser>() {
            public com.org.domain.LogUser convert(java.lang.Long id) {
                return LogUser.findLogUser(id);
            }
        };
    }

	public Converter<String, LogUser> getStringToLogUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.LogUser>() {
            public com.org.domain.LogUser convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LogUser.class);
            }
        };
    }

	public Converter<LogUserRole, String> getLogUserRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.LogUserRole, java.lang.String>() {
            public String convert(LogUserRole logUserRole) {
                return new StringBuilder().append(logUserRole.getRoleName()).toString();
            }
        };
    }

	public Converter<Long, LogUserRole> getIdToLogUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.LogUserRole>() {
            public com.org.domain.LogUserRole convert(java.lang.Long id) {
                return LogUserRole.findLogUserRole(id);
            }
        };
    }

	public Converter<String, LogUserRole> getStringToLogUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.LogUserRole>() {
            public com.org.domain.LogUserRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LogUserRole.class);
            }
        };
    }

	public Converter<Long, Aggreement> getIdToAggreementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Aggreement>() {
            public com.org.entity.Aggreement convert(java.lang.Long id) {
                return Aggreement.findAggreement(id);
            }
        };
    }

	public Converter<String, Aggreement> getStringToAggreementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Aggreement>() {
            public com.org.entity.Aggreement convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Aggreement.class);
            }
        };
    }

	public Converter<Long, Company> getIdToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Company>() {
            public com.org.entity.Company convert(java.lang.Long id) {
                return Company.findCompany(id);
            }
        };
    }

	public Converter<String, Company> getStringToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Company>() {
            public com.org.entity.Company convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Company.class);
            }
        };
    }

	public Converter<Document, String> getDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Document, java.lang.String>() {
            public String convert(Document document) {
                return new StringBuilder().append(document.getWorkbook()).append(' ').append(document.getExcelFile()).append(' ').append(document.getName()).append(' ').append(document.getDescription()).toString();
            }
        };
    }

	public Converter<Long, Document> getIdToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Document>() {
            public com.org.entity.Document convert(java.lang.Long id) {
                return Document.findDocument(id);
            }
        };
    }

	public Converter<String, Document> getStringToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Document>() {
            public com.org.entity.Document convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Document.class);
            }
        };
    }

	public Converter<Estimate, String> getEstimateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Estimate, java.lang.String>() {
            public String convert(Estimate estimate) {
                return new StringBuilder().append(estimate.getNameOfWork()).append(' ').append(estimate.getContent()).append(' ').append(estimate.getUrl()).toString();
            }
        };
    }

	public Converter<Long, Estimate> getIdToEstimateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Estimate>() {
            public com.org.entity.Estimate convert(java.lang.Long id) {
                return Estimate.findEstimate(id);
            }
        };
    }

	public Converter<String, Estimate> getStringToEstimateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Estimate>() {
            public com.org.entity.Estimate convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Estimate.class);
            }
        };
    }

	public Converter<Item, String> getItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Item, java.lang.String>() {
            public String convert(Item item) {
                return new StringBuilder().append(item.getFullDescription()).append(' ').append(item.getSubItemNumber()).append(' ').append(item.getItemNumber()).append(' ').append(item.getSortOrder()).toString();
            }
        };
    }

	public Converter<Long, Item> getIdToItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Item>() {
            public com.org.entity.Item convert(java.lang.Long id) {
                return Item.findItem(id);
            }
        };
    }

	public Converter<String, Item> getStringToItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Item>() {
            public com.org.entity.Item convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Item.class);
            }
        };
    }

	public Converter<ItemAbstract, String> getItemAbstractToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.ItemAbstract, java.lang.String>() {
            public String convert(ItemAbstract itemAbstract) {
                return new StringBuilder().append(itemAbstract.getTotal()).append(' ').append(itemAbstract.getAbsCellRef()).append(' ').append(itemAbstract.getMeasCellRef()).append(' ').append(itemAbstract.getPartRateRef()).toString();
            }
        };
    }

	public Converter<Long, ItemAbstract> getIdToItemAbstractConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ItemAbstract>() {
            public com.org.entity.ItemAbstract convert(java.lang.Long id) {
                return ItemAbstract.findItemAbstract(id);
            }
        };
    }

	public Converter<String, ItemAbstract> getStringToItemAbstractConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ItemAbstract>() {
            public com.org.entity.ItemAbstract convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItemAbstract.class);
            }
        };
    }

	public Converter<Long, ItemName> getIdToItemNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ItemName>() {
            public com.org.entity.ItemName convert(java.lang.Long id) {
                return ItemName.findItemName(id);
            }
        };
    }

	public Converter<String, ItemName> getStringToItemNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ItemName>() {
            public com.org.entity.ItemName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItemName.class);
            }
        };
    }

	public Converter<Long, Labour> getIdToLabourConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Labour>() {
            public com.org.entity.Labour convert(java.lang.Long id) {
                return Labour.findLabour(id);
            }
        };
    }

	public Converter<String, Labour> getStringToLabourConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Labour>() {
            public com.org.entity.Labour convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Labour.class);
            }
        };
    }

	public Converter<LabourEntry, String> getLabourEntryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.LabourEntry, java.lang.String>() {
            public String convert(LabourEntry labourEntry) {
                return new StringBuilder().append(labourEntry.getCreatedOn()).append(' ').append(labourEntry.getLastUpdatedOn()).append(' ').append(labourEntry.getDate()).append(' ').append(labourEntry.getLocation()).toString();
            }
        };
    }

	public Converter<Long, LabourEntry> getIdToLabourEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.LabourEntry>() {
            public com.org.entity.LabourEntry convert(java.lang.Long id) {
                return LabourEntry.findLabourEntry(id);
            }
        };
    }

	public Converter<String, LabourEntry> getStringToLabourEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.LabourEntry>() {
            public com.org.entity.LabourEntry convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabourEntry.class);
            }
        };
    }

	public Converter<Long, LabourSupplier> getIdToLabourSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.LabourSupplier>() {
            public com.org.entity.LabourSupplier convert(java.lang.Long id) {
                return LabourSupplier.findLabourSupplier(id);
            }
        };
    }

	public Converter<String, LabourSupplier> getStringToLabourSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.LabourSupplier>() {
            public com.org.entity.LabourSupplier convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabourSupplier.class);
            }
        };
    }

	public Converter<ManagedDocument, String> getManagedDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.ManagedDocument, java.lang.String>() {
            public String convert(ManagedDocument managedDocument) {
                return new StringBuilder().append(managedDocument.getDescription()).append(' ').append(managedDocument.getContent()).append(' ').append(managedDocument.getFileSize()).append(' ').append(managedDocument.getUrl()).toString();
            }
        };
    }

	public Converter<Long, ManagedDocument> getIdToManagedDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ManagedDocument>() {
            public com.org.entity.ManagedDocument convert(java.lang.Long id) {
                return ManagedDocument.findManagedDocument(id);
            }
        };
    }

	public Converter<String, ManagedDocument> getStringToManagedDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ManagedDocument>() {
            public com.org.entity.ManagedDocument convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ManagedDocument.class);
            }
        };
    }

	public Converter<MaterialEntry, String> getMaterialEntryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.MaterialEntry, java.lang.String>() {
            public String convert(MaterialEntry materialEntry) {
                return new StringBuilder().append(materialEntry.getCreatedOn()).append(' ').append(materialEntry.getLastUpdatedOn()).append(' ').append(materialEntry.getSite()).append(' ').append(materialEntry.getDescriptionOfItem1()).toString();
            }
        };
    }

	public Converter<Long, MaterialEntry> getIdToMaterialEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.MaterialEntry>() {
            public com.org.entity.MaterialEntry convert(java.lang.Long id) {
                return MaterialEntry.findMaterialEntry(id);
            }
        };
    }

	public Converter<String, MaterialEntry> getStringToMaterialEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.MaterialEntry>() {
            public com.org.entity.MaterialEntry convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MaterialEntry.class);
            }
        };
    }

	public Converter<MeasurementSheet, String> getMeasurementSheetToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.MeasurementSheet, java.lang.String>() {
            public String convert(MeasurementSheet measurementSheet) {
                return new StringBuilder().append(measurementSheet.getSerialNumberDisplayFormat()).append(' ').append(measurementSheet.getTemplateVersion()).append(' ').append(measurementSheet.getTitle()).append(' ').append(measurementSheet.getSerialNumber()).toString();
            }
        };
    }

	public Converter<Long, MeasurementSheet> getIdToMeasurementSheetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.MeasurementSheet>() {
            public com.org.entity.MeasurementSheet convert(java.lang.Long id) {
                return MeasurementSheet.findMeasurementSheet(id);
            }
        };
    }

	public Converter<String, MeasurementSheet> getStringToMeasurementSheetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.MeasurementSheet>() {
            public com.org.entity.MeasurementSheet convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MeasurementSheet.class);
            }
        };
    }

	public Converter<Long, Supplier> getIdToSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Supplier>() {
            public com.org.entity.Supplier convert(java.lang.Long id) {
                return Supplier.findSupplier(id);
            }
        };
    }

	public Converter<String, Supplier> getStringToSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Supplier>() {
            public com.org.entity.Supplier convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Supplier.class);
            }
        };
    }

	public Converter<Template, String> getTemplateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Template, java.lang.String>() {
            public String convert(Template template) {
                return new StringBuilder().append(template.getName()).append(' ').append(template.getDescription()).append(' ').append(template.getContentType()).append(' ').append(template.getFileSize()).toString();
            }
        };
    }

	public Converter<Long, Template> getIdToTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Template>() {
            public com.org.entity.Template convert(java.lang.Long id) {
                return Template.findTemplate(id);
            }
        };
    }

	public Converter<String, Template> getStringToTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Template>() {
            public com.org.entity.Template convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Template.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getConfigToStringConverter());
        registry.addConverter(getIdToConfigConverter());
        registry.addConverter(getStringToConfigConverter());
        registry.addConverter(getLogUserToStringConverter());
        registry.addConverter(getIdToLogUserConverter());
        registry.addConverter(getStringToLogUserConverter());
        registry.addConverter(getLogUserRoleToStringConverter());
        registry.addConverter(getIdToLogUserRoleConverter());
        registry.addConverter(getStringToLogUserRoleConverter());
        registry.addConverter(getAggreementToStringConverter());
        registry.addConverter(getIdToAggreementConverter());
        registry.addConverter(getStringToAggreementConverter());
        registry.addConverter(getCompanyToStringConverter());
        registry.addConverter(getIdToCompanyConverter());
        registry.addConverter(getStringToCompanyConverter());
        registry.addConverter(getDocumentToStringConverter());
        registry.addConverter(getIdToDocumentConverter());
        registry.addConverter(getStringToDocumentConverter());
        registry.addConverter(getEstimateToStringConverter());
        registry.addConverter(getIdToEstimateConverter());
        registry.addConverter(getStringToEstimateConverter());
        registry.addConverter(getItemToStringConverter());
        registry.addConverter(getIdToItemConverter());
        registry.addConverter(getStringToItemConverter());
        registry.addConverter(getItemAbstractToStringConverter());
        registry.addConverter(getIdToItemAbstractConverter());
        registry.addConverter(getStringToItemAbstractConverter());
        registry.addConverter(getItemNameToStringConverter());
        registry.addConverter(getIdToItemNameConverter());
        registry.addConverter(getStringToItemNameConverter());
        registry.addConverter(getLabourToStringConverter());
        registry.addConverter(getIdToLabourConverter());
        registry.addConverter(getStringToLabourConverter());
        registry.addConverter(getLabourEntryToStringConverter());
        registry.addConverter(getIdToLabourEntryConverter());
        registry.addConverter(getStringToLabourEntryConverter());
        registry.addConverter(getLabourSupplierToStringConverter());
        registry.addConverter(getIdToLabourSupplierConverter());
        registry.addConverter(getStringToLabourSupplierConverter());
        registry.addConverter(getManagedDocumentToStringConverter());
        registry.addConverter(getIdToManagedDocumentConverter());
        registry.addConverter(getStringToManagedDocumentConverter());
        registry.addConverter(getMaterialEntryToStringConverter());
        registry.addConverter(getIdToMaterialEntryConverter());
        registry.addConverter(getStringToMaterialEntryConverter());
        registry.addConverter(getMeasurementSheetToStringConverter());
        registry.addConverter(getIdToMeasurementSheetConverter());
        registry.addConverter(getStringToMeasurementSheetConverter());
        registry.addConverter(getSupplierToStringConverter());
        registry.addConverter(getIdToSupplierConverter());
        registry.addConverter(getStringToSupplierConverter());
        registry.addConverter(getTemplateToStringConverter());
        registry.addConverter(getIdToTemplateConverter());
        registry.addConverter(getStringToTemplateConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
