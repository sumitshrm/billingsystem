package com.org.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.domain.LogUser;
import com.org.entity.ItemName;
import com.org.entity.LabourEntry;
import com.org.entity.LabourSupplier;
import com.org.entity.MaterialEntry;
import com.org.util.QueryUtil;

@Service
public class LabourEntryService {

	@Transactional
	public void create(LabourEntry labourEntry){
		populateData(labourEntry);
		labourEntry.persist();
	}
	
	@Transactional
	public void udate(LabourEntry labourEntry){
		populateData(labourEntry);
		labourEntry.merge();
	}
	
	private void populateData(LabourEntry labourEntry){
		LogUser currentUser = LogUser.getCurrentUser();
		LabourSupplier labourSupplier = null;
		if(!"".equals(labourEntry.getLabourSupplier().getName().trim())){
			String labourSupplierName = labourEntry.getLabourSupplier().getName();
			labourSupplier = QueryUtil.getFirstResult(LabourSupplier.findLabourSuppliersByNameLikeAndCreatedBy(labourSupplierName, currentUser));
			if(labourSupplier == null){
				labourSupplier = new LabourSupplier();
				labourSupplier.setName(labourSupplierName);
				//labourSupplier.persist();
			}
		}
		labourEntry.setLabourSupplier(labourSupplier);
	}
	
}
