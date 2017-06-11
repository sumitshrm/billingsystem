package com.org.entity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.poi.util.StringUtil;
import org.hibernate.annotations.Cascade;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.org.domain.LogUser;
import com.org.util.StrUtil;
import com.org.util.Unit;

import org.springframework.beans.factory.annotation.Value;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findItemsByItemNumber", "findItemsByLogUser", "findItemsByIdAndLogUser", "findItemsByItemNumberAndAggreement", "findItemsByAggreementAndIsExtraItem", "findItemsByAggreementAndMeasurementSheetIdIsNullOrMeasurementSheetIdNotEquals", "findItemsByAggreementAndMeasurementSheetId", "findItemsByAggreement", "findItemsByAggreementAndIsExtraItemAndParentItemIsNull", "findItemsByAggreementAndLogUser" })
public class Item {

    @NotNull
    private String itemNumber;

    /**
     * this attribute is used in JSON only.
     */
    @Transient
    private String subItemNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Aggreement aggreement;

    @ManyToOne()
    @JoinColumn(name = "parent_id")
    private Item parentItem;

    @OneToMany(mappedBy = "parentItem", cascade = CascadeType.REMOVE)
    @OrderBy("sortOrder DESC")
    private List<Item> subItems;

    private Long sortOrder;

    @NotNull
    @Size(max = 2000)
    private String description;

    private String unit;

    @Value("1.0")
    private Double quantityPerUnit;

    @NotNull
    private boolean isExtraItem;

    @Column(precision = 12, scale = 2)
    private Double fullRate;

    @Column(precision = 12, scale = 2)
    private Double partRate;

    private Double quantity;

    private String drsCode;
    
    private Double dsrRate;

    /**
     * it this is an extra item. it should be associated with some measruementsheet where it came from.
     */
    private Long measurementSheetId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<ItemAbstract> itemAbstracts;

    @ManyToOne(fetch = FetchType.EAGER)
    private LogUser logUser;


    @Transient
    private transient String fullDescription;

    @Transient
    private transient String parentItemDescription;

    @Transient
    private transient String delimiter = " ";

    @Transient
    private transient String seperator = ". ";

    public Item() {
        super();
    }

    public Item(String itemNumber, String subItemNumber) {
        super();
        this.itemNumber = itemNumber;
    }

    @PrePersist
    public void prePersist() {
        setUserDetails();
    }

    private void setUserDetails() {
        this.setLogUser(LogUser.getCurrentUser());
    }

    public String getItemNumDisplayFormat() {
    	int pos = itemNumber.lastIndexOf(".");
    	if(pos > 0){ 
    		return itemNumber.substring(pos+1);
    	}
        return itemNumber;
    }

    public String getItemNumExcelFormat() {
        return itemNumber;
    }

    public boolean isValidItem() {
        return this.getSubItems()==null ? true : this.getSubItems().size() == 0;
    }

    public String getFullDescription() {
        return getDescription(this);
    }
    
    public String getFullDescriptionHTML() {
        return getDescription(this, 140, true);
    }

    private String getDescription(Item item) {
    	return getDescription(item, -1, false);
    }
    
	private String getDescription(Item item, int maxLength, boolean isHTML) { 
        String description = item.getDescription();
        int ml = (description.length() < maxLength)?description.length():maxLength;
        if(maxLength>0 && description.length() > maxLength){
	        description=description.substring(0, ml)+"...";
        }
        if (item.getParentItem() != null) {
            description = getDescription(item.getParentItem(), maxLength, isHTML) + "\n" + item.getItemNumDisplayFormat() + ") " + description;
        }
        if(isHTML){
        	return StrUtil.toHTMLUtill(description);
        }
        return description;
    }

    public String getSubItemNumber() {
        return this.subItemNumber;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setParentItemDescription(String parentItemDescription) {
        this.parentItemDescription = parentItemDescription;
    }

    public boolean isIsSubItem() {
        return this.getParentItem() != null;
    }

    public static String getFullItemNumber(String itemNum, String subItemNum) {
        return itemNum + " " + subItemNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((itemNumber == null) ? 0 : itemNumber.hashCode());
        return result;
    }

    public Double getQuantityPerUnitValue() {
        return this.getQuantity() == null ? 1.0 : this.getQuantity();
    }

    public Double getAmount() {
        return quantity * partRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Item other = (Item) obj;
        if (itemNumber == null) {
            if (other.itemNumber != null) return false;
        } else if (!itemNumber.equals(other.itemNumber)) return false;
        return true;
    }
    
    public String getDescriptionHTML(){
		return description != null ? description.replaceAll("(\r\n|\n)",
				"<br />") : "";
    }
    
    public void addParentItem(Item parentItem){ 
    	this.setParentItem(parentItem);
    	List<Item> subItems = parentItem.getSubItems();
    	if(subItems==null){
    		subItems = new ArrayList<Item>();
    		parentItem.setSubItems(subItems);
    	}
    	subItems.add(this);
    }
    
    public boolean validate(){
    	if(isValidItem() && (getFullRate()==null)){
    		return false;
    	}
    	return true;
    }
    
    public String getValidationMessage(){
    	if(getFullRate()==null){
    		return "Full rate cannot be blank for item number : "+ getItemNumber();
    	}
    	return "Validation failed for item number : " + getItemNumber();
    }
    
    /*@Transactional
    public void remove() {
    	EntityManager entityManager = entityManager();
        if (entityManager == null) this.entityManager = entityManager();
        if (this.parentItem!=null){
        	//this.parentItem.getSubItems().remove(this);
        }
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Item attached = Item.findItem(this.getId());
            this.entityManager.remove(attached);
        }
    }*/
}
