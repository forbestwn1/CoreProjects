package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;

//container for entity
public class HAPContainerEntity {

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private Set<HAPInfoContainerElement> m_elements;
	
	public HAPContainerEntity() {
		this.m_elements = new HashSet<HAPInfoContainerElement>();
	}
	
	public List<HAPInfoContainerElement> getElements(){   return this.getElements(); 	}
	
	public HAPIdEntityInDomain getElement(HAPInfoContainerElement eleInfo) {  return null;  }

	public void addEntityElement(HAPIdEntityInDomain eleEntityId, HAPInfoContainerElement eleInfo) {    }
	
	public HAPContainerEntity cloneContainerEntity() {
		
	}
	
	
	
	
//	public HAPIdEntityInDomain getElementByName(String name) {    }
//	
//	public HAPIdEntityInDomain getElementById(HAPIdEntityInDomain id) {    }
//	
//	public void addEntityElement(HAPIdEntityInDomain entityId) {  this.m_elements.add(entityId);  }
//
//	public void addEntityElement(HAPInfoContainerElement eleInfo) {    }
	
}
