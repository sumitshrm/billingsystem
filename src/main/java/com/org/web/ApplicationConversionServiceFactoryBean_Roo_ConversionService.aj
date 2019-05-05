// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.web;

import com.org.domain.Config;
import com.org.domain.LogUser;
import com.org.domain.LogUserRole;
import com.org.entity.Aggreement;
import com.org.entity.Company;
import com.org.entity.Document;
import com.org.entity.Estimate;
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
import com.org.web.ApplicationConversionServiceFactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Config, String> ApplicationConversionServiceFactoryBean.getConfigToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.Config, java.lang.String>() {
            public String convert(Config config) {
                return new StringBuilder().append(config.getCellName()).append(' ').append(config.getValue()).toString();
            }
        };
    }
    
    public Converter<Long, Config> ApplicationConversionServiceFactoryBean.getIdToConfigConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.Config>() {
            public com.org.domain.Config convert(java.lang.Long id) {
                return Config.findConfig(id);
            }
        };
    }
    
    public Converter<String, Config> ApplicationConversionServiceFactoryBean.getStringToConfigConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.Config>() {
            public com.org.domain.Config convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Config.class);
            }
        };
    }
    
    public Converter<LogUser, String> ApplicationConversionServiceFactoryBean.getLogUserToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.LogUser, java.lang.String>() {
            public String convert(LogUser logUser) {
                return new StringBuilder().append(logUser.getUsername()).append(' ').append(logUser.getFullName()).append(' ').append(logUser.getPassword()).append(' ').append(logUser.getEmailAddress()).toString();
            }
        };
    }
    
    public Converter<Long, LogUser> ApplicationConversionServiceFactoryBean.getIdToLogUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.LogUser>() {
            public com.org.domain.LogUser convert(java.lang.Long id) {
                return LogUser.findLogUser(id);
            }
        };
    }
    
    public Converter<String, LogUser> ApplicationConversionServiceFactoryBean.getStringToLogUserConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.LogUser>() {
            public com.org.domain.LogUser convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LogUser.class);
            }
        };
    }
    
    public Converter<LogUserRole, String> ApplicationConversionServiceFactoryBean.getLogUserRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.domain.LogUserRole, java.lang.String>() {
            public String convert(LogUserRole logUserRole) {
                return new StringBuilder().append(logUserRole.getRoleName()).toString();
            }
        };
    }
    
    public Converter<Long, LogUserRole> ApplicationConversionServiceFactoryBean.getIdToLogUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.domain.LogUserRole>() {
            public com.org.domain.LogUserRole convert(java.lang.Long id) {
                return LogUserRole.findLogUserRole(id);
            }
        };
    }
    
    public Converter<String, LogUserRole> ApplicationConversionServiceFactoryBean.getStringToLogUserRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.domain.LogUserRole>() {
            public com.org.domain.LogUserRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LogUserRole.class);
            }
        };
    }
    
    public Converter<Long, Aggreement> ApplicationConversionServiceFactoryBean.getIdToAggreementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Aggreement>() {
            public com.org.entity.Aggreement convert(java.lang.Long id) {
                return Aggreement.findAggreement(id);
            }
        };
    }
    
    public Converter<String, Aggreement> ApplicationConversionServiceFactoryBean.getStringToAggreementConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Aggreement>() {
            public com.org.entity.Aggreement convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Aggreement.class);
            }
        };
    }
    
    public Converter<Long, Company> ApplicationConversionServiceFactoryBean.getIdToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Company>() {
            public com.org.entity.Company convert(java.lang.Long id) {
                return Company.findCompany(id);
            }
        };
    }
    
    public Converter<String, Company> ApplicationConversionServiceFactoryBean.getStringToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Company>() {
            public com.org.entity.Company convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Company.class);
            }
        };
    }
    
    public Converter<Document, String> ApplicationConversionServiceFactoryBean.getDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Document, java.lang.String>() {
            public String convert(Document document) {
                return new StringBuilder().append(document.getWorkbook()).append(' ').append(document.getExcelFile()).append(' ').append(document.getName()).append(' ').append(document.getDescription()).toString();
            }
        };
    }
    
    public Converter<Long, Document> ApplicationConversionServiceFactoryBean.getIdToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Document>() {
            public com.org.entity.Document convert(java.lang.Long id) {
                return Document.findDocument(id);
            }
        };
    }
    
    public Converter<String, Document> ApplicationConversionServiceFactoryBean.getStringToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Document>() {
            public com.org.entity.Document convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Document.class);
            }
        };
    }
    
    public Converter<Estimate, String> ApplicationConversionServiceFactoryBean.getEstimateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Estimate, java.lang.String>() {
            public String convert(Estimate estimate) {
                return new StringBuilder().append(estimate.getNameOfWork()).append(' ').append(estimate.getContent()).append(' ').append(estimate.getUrl()).toString();
            }
        };
    }
    
    public Converter<Long, Estimate> ApplicationConversionServiceFactoryBean.getIdToEstimateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Estimate>() {
            public com.org.entity.Estimate convert(java.lang.Long id) {
                return Estimate.findEstimate(id);
            }
        };
    }
    
    public Converter<String, Estimate> ApplicationConversionServiceFactoryBean.getStringToEstimateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Estimate>() {
            public com.org.entity.Estimate convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Estimate.class);
            }
        };
    }
    
    public Converter<ItemAbstract, String> ApplicationConversionServiceFactoryBean.getItemAbstractToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.ItemAbstract, java.lang.String>() {
            public String convert(ItemAbstract itemAbstract) {
                return new StringBuilder().append(itemAbstract.getTotal()).append(' ').append(itemAbstract.getAbsCellRef()).append(' ').append(itemAbstract.getMeasCellRef()).append(' ').append(itemAbstract.getPartRateRef()).toString();
            }
        };
    }
    
    public Converter<Long, ItemAbstract> ApplicationConversionServiceFactoryBean.getIdToItemAbstractConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ItemAbstract>() {
            public com.org.entity.ItemAbstract convert(java.lang.Long id) {
                return ItemAbstract.findItemAbstract(id);
            }
        };
    }
    
    public Converter<String, ItemAbstract> ApplicationConversionServiceFactoryBean.getStringToItemAbstractConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ItemAbstract>() {
            public com.org.entity.ItemAbstract convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItemAbstract.class);
            }
        };
    }
    
    public Converter<Long, ItemName> ApplicationConversionServiceFactoryBean.getIdToItemNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ItemName>() {
            public com.org.entity.ItemName convert(java.lang.Long id) {
                return ItemName.findItemName(id);
            }
        };
    }
    
    public Converter<String, ItemName> ApplicationConversionServiceFactoryBean.getStringToItemNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ItemName>() {
            public com.org.entity.ItemName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ItemName.class);
            }
        };
    }
    
    public Converter<Long, Labour> ApplicationConversionServiceFactoryBean.getIdToLabourConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Labour>() {
            public com.org.entity.Labour convert(java.lang.Long id) {
                return Labour.findLabour(id);
            }
        };
    }
    
    public Converter<String, Labour> ApplicationConversionServiceFactoryBean.getStringToLabourConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Labour>() {
            public com.org.entity.Labour convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Labour.class);
            }
        };
    }
    
    public Converter<LabourEntry, String> ApplicationConversionServiceFactoryBean.getLabourEntryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.LabourEntry, java.lang.String>() {
            public String convert(LabourEntry labourEntry) {
                return new StringBuilder().append(labourEntry.getCreatedOn()).append(' ').append(labourEntry.getLastUpdatedOn()).append(' ').append(labourEntry.getDate()).append(' ').append(labourEntry.getLocation()).toString();
            }
        };
    }
    
    public Converter<Long, LabourEntry> ApplicationConversionServiceFactoryBean.getIdToLabourEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.LabourEntry>() {
            public com.org.entity.LabourEntry convert(java.lang.Long id) {
                return LabourEntry.findLabourEntry(id);
            }
        };
    }
    
    public Converter<String, LabourEntry> ApplicationConversionServiceFactoryBean.getStringToLabourEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.LabourEntry>() {
            public com.org.entity.LabourEntry convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabourEntry.class);
            }
        };
    }
    
    public Converter<Long, LabourSupplier> ApplicationConversionServiceFactoryBean.getIdToLabourSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.LabourSupplier>() {
            public com.org.entity.LabourSupplier convert(java.lang.Long id) {
                return LabourSupplier.findLabourSupplier(id);
            }
        };
    }
    
    public Converter<String, LabourSupplier> ApplicationConversionServiceFactoryBean.getStringToLabourSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.LabourSupplier>() {
            public com.org.entity.LabourSupplier convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LabourSupplier.class);
            }
        };
    }
    
    public Converter<ManagedDocument, String> ApplicationConversionServiceFactoryBean.getManagedDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.ManagedDocument, java.lang.String>() {
            public String convert(ManagedDocument managedDocument) {
                return new StringBuilder().append(managedDocument.getDescription()).append(' ').append(managedDocument.getContent()).append(' ').append(managedDocument.getFileSize()).append(' ').append(managedDocument.getUrl()).toString();
            }
        };
    }
    
    public Converter<Long, ManagedDocument> ApplicationConversionServiceFactoryBean.getIdToManagedDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.ManagedDocument>() {
            public com.org.entity.ManagedDocument convert(java.lang.Long id) {
                return ManagedDocument.findManagedDocument(id);
            }
        };
    }
    
    public Converter<String, ManagedDocument> ApplicationConversionServiceFactoryBean.getStringToManagedDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.ManagedDocument>() {
            public com.org.entity.ManagedDocument convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ManagedDocument.class);
            }
        };
    }
    
    public Converter<MaterialEntry, String> ApplicationConversionServiceFactoryBean.getMaterialEntryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.MaterialEntry, java.lang.String>() {
            public String convert(MaterialEntry materialEntry) {
                return new StringBuilder().append(materialEntry.getCreatedOn()).append(' ').append(materialEntry.getLastUpdatedOn()).append(' ').append(materialEntry.getSite()).append(' ').append(materialEntry.getDescriptionOfItem1()).toString();
            }
        };
    }
    
    public Converter<Long, MaterialEntry> ApplicationConversionServiceFactoryBean.getIdToMaterialEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.MaterialEntry>() {
            public com.org.entity.MaterialEntry convert(java.lang.Long id) {
                return MaterialEntry.findMaterialEntry(id);
            }
        };
    }
    
    public Converter<String, MaterialEntry> ApplicationConversionServiceFactoryBean.getStringToMaterialEntryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.MaterialEntry>() {
            public com.org.entity.MaterialEntry convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MaterialEntry.class);
            }
        };
    }
    
    public Converter<MeasurementSheet, String> ApplicationConversionServiceFactoryBean.getMeasurementSheetToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.MeasurementSheet, java.lang.String>() {
            public String convert(MeasurementSheet measurementSheet) {
                return new StringBuilder().append(measurementSheet.getSerialNumberDisplayFormat()).append(' ').append(measurementSheet.getTemplateVersion()).append(' ').append(measurementSheet.getTitle()).append(' ').append(measurementSheet.getSerialNumber()).toString();
            }
        };
    }
    
    public Converter<Long, MeasurementSheet> ApplicationConversionServiceFactoryBean.getIdToMeasurementSheetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.MeasurementSheet>() {
            public com.org.entity.MeasurementSheet convert(java.lang.Long id) {
                return MeasurementSheet.findMeasurementSheet(id);
            }
        };
    }
    
    public Converter<String, MeasurementSheet> ApplicationConversionServiceFactoryBean.getStringToMeasurementSheetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.MeasurementSheet>() {
            public com.org.entity.MeasurementSheet convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), MeasurementSheet.class);
            }
        };
    }
    
    public Converter<Long, Supplier> ApplicationConversionServiceFactoryBean.getIdToSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Supplier>() {
            public com.org.entity.Supplier convert(java.lang.Long id) {
                return Supplier.findSupplier(id);
            }
        };
    }
    
    public Converter<String, Supplier> ApplicationConversionServiceFactoryBean.getStringToSupplierConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Supplier>() {
            public com.org.entity.Supplier convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Supplier.class);
            }
        };
    }
    
    public Converter<Template, String> ApplicationConversionServiceFactoryBean.getTemplateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.org.entity.Template, java.lang.String>() {
            public String convert(Template template) {
                return new StringBuilder().append(template.getName()).append(' ').append(template.getDescription()).append(' ').append(template.getContentType()).append(' ').append(template.getFileSize()).toString();
            }
        };
    }
    
    public Converter<Long, Template> ApplicationConversionServiceFactoryBean.getIdToTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.org.entity.Template>() {
            public com.org.entity.Template convert(java.lang.Long id) {
                return Template.findTemplate(id);
            }
        };
    }
    
    public Converter<String, Template> ApplicationConversionServiceFactoryBean.getStringToTemplateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.org.entity.Template>() {
            public com.org.entity.Template convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Template.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
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
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
