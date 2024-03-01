package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPDefinitionEntityContainerSimple extends HAPManualEntityComplex{

	public static final String ATTR_ELEMENT_TYPEINFO = "eleTypeInfo";

	public String addElementAttribute(HAPIdEntityInDomain entityId) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttributeSimple(attrName, new HAPEmbededDefinition(entityId));
		return attrName;
	}

	public String addElementAttribute(String eleId, HAPIdEntityInDomain entityId) {
		this.setAttributeSimple(eleId, new HAPEmbededDefinition(entityId));
		return eleId;
	}

	public String getElmentValueTypeInfo() {     return (String)this.getAttributeValue(ATTR_ELEMENT_TYPEINFO);     }
	public void setElementValueTypeInfo(String eleValueType) {    this.setAttributeValueObject(ATTR_ELEMENT_TYPEINFO, eleValueType);  }
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityContainerSimple out = new HAPDefinitionEntityContainerSimple();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
