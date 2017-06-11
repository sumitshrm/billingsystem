package com.org.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.domain.LogUser;
import com.org.entity.Company;
import com.org.entity.ItemName;
import com.org.entity.MaterialEntry;
import com.org.entity.Supplier;
import com.org.util.QueryUtil;

@Service
public class MaterialEntryService {

	@Transactional
	public void create(MaterialEntry materialEntry){
		populateData(materialEntry);
		materialEntry.persist();
	}
	
	@Transactional
	public void update(MaterialEntry materialEntry){
		populateData(materialEntry);
		materialEntry.merge();
	}
	
	private void populateData(MaterialEntry materialEntry){
		LogUser currentUser = LogUser.getCurrentUser();
		ItemName item = null;
		if(!"".equals(materialEntry.getItem().getName().trim())){
			String itemName = materialEntry.getItem().getName();
			item = QueryUtil.getFirstResult(ItemName.findItemNamesByNameLikeAndCreatedBy(itemName, currentUser));
			if(item == null){
				item = new ItemName(itemName);
				//item.persist();
			}
		}
		materialEntry.setItem(item);
		
		Company company = null;
		if(!"".equals(materialEntry.getCompany().getName().trim())){
			String companyName = materialEntry.getCompany().getName();
			company = QueryUtil.getFirstResult(Company.findCompanysByNameLikeAndCreatedBy(companyName, currentUser));
			if(company == null){
				company = new Company(companyName);
				//company.persist();
			}
		}
		materialEntry.setCompany(company);
		
		Supplier supplier = null;
		if(!"".equals(materialEntry.getSupplier().getName().trim())){
			String supplierName = materialEntry.getSupplier().getName();
			supplier = QueryUtil.getFirstResult(Supplier.findSuppliersByNameLikeAndCreatedBy(supplierName, currentUser));
			if(supplier == null){
				supplier = new Supplier(supplierName);
				//supplier.persist();
			}
		}
		materialEntry.setSupplier(supplier);
    }
}
