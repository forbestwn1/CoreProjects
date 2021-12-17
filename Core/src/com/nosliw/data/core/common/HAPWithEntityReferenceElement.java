package com.nosliw.data.core.common;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPWithEntityReferenceElement {

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private Set<HAPIdEntityInDomain> m_elements;
	
	public HAPWithEntityReferenceElement() {
		this.m_elements = new HashSet<HAPIdEntityInDomain>();
	}
	
	public Set<HAPIdEntityInDomain> getEntityElements(){   return this.m_elements; 	}
	
	public void addEntityElement(HAPIdEntityInDomain entityId) {  this.m_elements.add(entityId);  }

}
