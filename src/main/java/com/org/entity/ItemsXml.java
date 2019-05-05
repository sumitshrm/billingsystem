package com.org.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dsr-data")
public class ItemsXml {
	
	@XmlElement(name="record")
    private List<ItemsXMLData> entries;

    public List<ItemsXMLData> getEntries() {
        return entries;
    }

	@Override
	public String toString() {
		return "ItemsXml [entries=" + entries + "]";
	}
    
    
}