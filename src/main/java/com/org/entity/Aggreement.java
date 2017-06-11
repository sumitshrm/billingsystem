
package com.org.entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.context.SecurityContextHolder;

import com.org.domain.LogUser;
import com.org.excel.util.MeasurementComparatorBySerialNumber;
import com.org.util.Clause;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findAggreementsByLogUser", "findAggreementsByIdAndLogUser" })
public class Aggreement {

	private String division;
	
	private String subDivision;
	
    @NotNull
    @Size(max = 20)
    private String description;

    @NotNull
    @Size(max = 800)
    private String nameOfWork;

    @Size(max = 200)
    private String agency;

    @NotNull
    @Size(max = 40)
    private String aggreementNum;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateOfStart;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateOfCompletionS;
    
    //@Pattern(regexp="^$|\\d{2}-\\d{2}-\\d{4}", message = "enter data in 'DD-MM-YYYY' format")
    private String dateOfCompletionA;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateOfAbstract;

    private float clausePercentage;
    
    @Value("14")
    private float costIndex;
    
    @NumberFormat(style = Style.NUMBER, pattern = "#.##")
    private Double tenderCost;
    
    @NumberFormat(style = Style.NUMBER, pattern = "#.##")
    private Double estimatedCost;

    @Enumerated(EnumType.STRING)
    private Clause clause;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aggreement", fetch=FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private Set<Item> items = new HashSet<Item>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aggreement", fetch=FetchType.LAZY)
    @OrderBy("id DESC")
    private Set<MeasurementSheet> measurementSheets = new HashSet<MeasurementSheet>();

    @ManyToOne
    private LogUser logUser;
    
    public Item getItemByItemNumber(String itemnum){
		Item item = null;
		for(Item i : this.getItems()){
			if(i.getItemNumber().equals(itemnum)){
				return i;
			}
		}
		return item;
	}
    
    public int getNextSerialNumber() {
		List<MeasurementSheet> msheets = new ArrayList<MeasurementSheet>(this.getMeasurementSheets());
		if(msheets.size()>0){
			Collections.sort(msheets, new MeasurementComparatorBySerialNumber());
			return msheets.get(msheets.size()-1).getSerialNumber()+1;
		}else{
			return 1;
		}
	}
    
    public MeasurementSheet getMeasurementSheetBySerialNumber(int serialnumber){
    	for(MeasurementSheet msheet : this.getMeasurementSheets()){
    		if(msheet.getSerialNumber()==serialnumber){
    			return msheet;
    		}
    	}
    	return null;
    }
    
    @PrePersist
    public void prePersist(){
		setUserDetails();
    }

	private void setUserDetails() {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		LogUser user = LogUser.findLogUsersByUsernameEquals(username)
				.getSingleResult();
		this.setLogUser(user);
	}
	
}
