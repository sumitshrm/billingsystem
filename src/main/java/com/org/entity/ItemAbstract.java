package com.org.entity;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.org.util.Clause;
import com.org.util.QueryUtil;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findItemAbstractsByItem", "findItemAbstractsByMeasurementSheetAndItem", "findItemAbstractsByMeasurementSheet" })
public class ItemAbstract {

    @ManyToOne
    private MeasurementSheet measurementSheet;

    @ManyToOne
    private Item item;

    private double total;
    
    private String absCellRef;
    
    private String measCellRef;
    
    private String partRateRef;
    
    private String fullRateRef;
    
    @Transient
    private List<ItemAbstractDataTo> itemDataTos = new ArrayList<ItemAbstractDataTo>();
    
    @Transient
    private transient DecimalFormat df = new DecimalFormat("#.00");

    public ItemAbstract() {
    }
    
    

    public ItemAbstract(double total, String absCellRef, String measCellRef) {
		super();
		this.total = total;
		this.absCellRef = absCellRef;
		this.measCellRef = measCellRef;
	}



	public ItemAbstract(MeasurementSheet msheet, Item item) {
        this.item = item;
        this.measurementSheet = msheet;
    }

    public MeasurementSheet getMeasurementSheet() {
        return measurementSheet;
    }

    public void setMeasurementSheet(MeasurementSheet measurementSheet) {
        this.measurementSheet = measurementSheet;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getTotal() {
        return format(total);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAbsCellRef() {
		return absCellRef;
	}



	public void setAbsCellRef(String absCellRef) {
		this.absCellRef = absCellRef;
	}



	public String getMeasCellRef() {
		return measCellRef;
	}



	public void setMeasCellRef(String measCellRef) {
		this.measCellRef = measCellRef;
	}



	public List<ItemAbstractDataTo> getItemDataTos() {
		return itemDataTos;
	}



	public void setItemDataTos(List<ItemAbstractDataTo> data) {
		this.itemDataTos = data;
	}



	public String getPartRateRef() {
		return partRateRef;
	}



	public void setPartRateRef(String partRateRef) {
		this.partRateRef = partRateRef;
	}



	public String getFullRateRef() {
		return fullRateRef;
	}



	public void setFullRateRef(String fullRateRef) {
		this.fullRateRef = fullRateRef;
	}



	public Double getAmount() {
		double rate = measurementSheet.isIsFinalBill() ? toDoubleValue(item.getFullRate())
				: toDoubleValue(item.getPartRate());
		double qtyPerUnit = item.getQuantityPerUnit() == null
				|| item.getQuantityPerUnit() == 0.0 ? 1 : item
				.getQuantityPerUnit();
		return format((total * rate) / qtyPerUnit);
	}

    public Double getAmountUptoPreviousBill() {
        MeasurementSheet msheet = measurementSheet.getPreviousMeasurementSheet();
        if (msheet == null) {
            return 0.0;
        } else {
            ItemAbstract abs = ItemAbstract.getItemAbstractByItemAndMeasurementSheet(item, msheet, false);
            if (abs == null) {
                return 0.0;
            } else {
                return abs.getAmount();
            }
        }
    }

    public Double getAmountSincePreviousBill() {
        return format(toDoubleValue(getAmount()) - toDoubleValue(getAmountUptoPreviousBill()));
    }

    public Double getDeviationQuantity() {
        return format(toDoubleValue(total) - toDoubleValue(item.getQuantity()));
    }

    public Double getContractorsAbattment() {
        return item.getAggreement().getClausePercentage() > 0 ? (format((toDoubleValue(item.getFullRate()) * item.getAggreement().getClausePercentage()) / 100)) : 0;
    }

    public Double getRateProposed() {
        if (item.getAggreement().getClause() == Clause.Above) {
            return format(toDoubleValue(item.getFullRate()) + getContractorsAbattment());
        } else {
            return format(toDoubleValue(item.getFullRate()) - getContractorsAbattment());
        }
    }

    public double getDeviationPercentate() {
    	if(item.getQuantity()==null||item.getQuantity()==0.0){
    		return 0.0;
    	}
        return format((getDeviationQuantity() / item.getQuantity()) * 100);
    }

    public double getDeviationPlus() {
        return getDeviationPercentate() > 10 ? Math.abs(Math.round((getDeviationQuantity() * getRateProposed()) / getQuantityPerUnit())) : 0;
    }

    public double getDeviationMinus() {
        return getDeviationPercentate() < -10 ? Math.abs(Math.round((getDeviationQuantity() * getRateProposed()) / getQuantityPerUnit())) : 0;
    }

    public double getTotalDeviation() {
        return getDeviationPlus() + getDeviationMinus();
    }
    
    public double getQuantityPerUnit(){
		return item.getQuantityPerUnit() == 0 ? 1 : item.getQuantityPerUnit();
    }

    private double toDoubleValue(Double d) {
        return d == null ? 0 : d;
    }

    private double format(Double d) {
        return d == null ? 0 : Double.parseDouble(df.format(d));
    }

    public static ItemAbstract getItemAbstractByItemAndMeasurementSheet(Item item, MeasurementSheet msheet, boolean createNew) {
        ItemAbstract result = QueryUtil.getUniqueResult(ItemAbstract.findItemAbstractsByMeasurementSheetAndItem(msheet, item));
        if (result == null && createNew) {
            result = new ItemAbstract();
            result.setItem(item);
            result.setMeasurementSheet(msheet);
            msheet.getItemAbstracts().add(result);
        }
        return result;
    }
}
