package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityContainerSimple extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_ELEMENT_TYPEINFO = "eleTypeInfo";

	public String addElementAttribute(HAPIdEntityInDomain entityId) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttributeSimple(attrName, new HAPEmbededDefinition(entityId), this.getElmentValueTypeInfo());
		return attrName;
	}

	public String addElementAttribute(String eleId, HAPIdEntityInDomain entityId) {
		this.setAttributeSimple(eleId, new HAPEmbededDefinition(entityId), this.getElmentValueTypeInfo());
		return eleId;
	}

	public String getElmentValueTypeInfo() {     return (String)this.getAttributeValue(ATTR_ELEMENT_TYPEINFO);     }
	public void setElementValueTypeInfo(String eleValueType) {    this.setAttributeValueObject(ATTR_ELEMENT_TYPEINFO, eleValueType);  }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityContainerSimple out = new HAPDefinitionEntityContainerSimple();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
