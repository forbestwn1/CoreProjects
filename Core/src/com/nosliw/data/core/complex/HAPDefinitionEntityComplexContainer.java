package com.nosliw.data.core.complex;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//complex entity that is also a container for other entity
public class HAPDefinitionEntityComplexContainer extends HAPDefinitionEntityComplex{

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private Set<HAPIdEntityInDomain> m_elements;
	
	public HAPDefinitionEntityComplexContainer(String entityType) {
		super(entityType);
		this.m_elements = new HashSet<HAPIdEntityInDomain>();
	}
	
	public Set<HAPIdEntityInDomain> getEntityElements(){   return this.m_elements; 	}
	
	public void addEntityElement(HAPIdEntityInDomain entityId) {  this.m_elements.add(entityId);  }


}
