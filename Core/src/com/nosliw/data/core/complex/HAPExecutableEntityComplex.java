package com.nosliw.data.core.complex;

import java.util.Map;

import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public abstract class HAPExecutableEntityComplex extends HAPExecutableImpEntityInfo{

	private String m_valueStructureComplexId;
	
	//simple attribute by name
	private Map<String, HAPIdEntityInDomain> m_attributesSimple;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	public abstract String getEntityType();
	
	public void setValueStructureComplexId(String id) {     this.m_valueStructureComplexId = id;      }
	public String getValueStructureComplexId() {    return this.m_valueStructureComplexId;    }
	
	public void setSimpleComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExeId) {    this.m_attributesSimple.put(attrName, complexEntityExeId);    }
	
	public void addContainerAttributeElementComplex(String attribute, HAPInfoContainerElement elementInfo) {
		
	}
}
