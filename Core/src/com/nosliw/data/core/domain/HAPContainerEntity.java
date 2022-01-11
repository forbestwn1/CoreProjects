package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;

//container for entity
public class HAPContainerEntity {

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private Set<HAPIdEntityInDomain> m_elements;
	
	public HAPContainerEntity() {
		this.m_elements = new HashSet<HAPIdEntityInDomain>();
	}
	
	public List<HAPInfoContainerElement> getElements(){   return null; 	}
	
	public HAPIdEntityInDomain getElementByName(String name) {    }
	
	public HAPIdEntityInDomain getElementById(HAPIdEntityInDomain id) {    }
	
	public void addEntityElement(HAPIdEntityInDomain entityId) {  this.m_elements.add(entityId);  }

	public void addEntityElement(HAPInfoContainerElement eleInfo) {    }
	
}
