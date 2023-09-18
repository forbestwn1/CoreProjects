package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoValueType;

public class HAPDefinitionEntityComplexContainer extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_ELEMENTRELATIONCONFIGURE = "elementRelationCoonfigure";

	public static final String ATTR_ELEMENT_TYPEINFO = "eleTypeInfo";

	public String addElementAttribute(HAPIdEntityInDomain entityId) {
		return this.addElementAttribute(entityId, true);
	}

	public String addElementAttribute(HAPIdEntityInDomain entityId, Boolean autoProcess) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttribute(attrName, new HAPEmbededDefinition(entityId), this.getElmentValueTypeInfo(), autoProcess);
		return attrName;
	}
	
	public HAPInfoValueType getElmentValueTypeInfo() {     return (HAPInfoValueType)this.getAttributeValue(ATTR_ELEMENT_TYPEINFO);     }
	public void setElementValueTypeInfo(HAPInfoValueType eleValueTypeInfo) {    this.setAttributeValueObject(ATTR_ELEMENT_TYPEINFO, eleValueTypeInfo);      }
	
	public HAPConfigureParentRelationComplex getElementRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(ATTR_ELEMENTRELATIONCONFIGURE, new HAPConfigureParentRelationComplex());     }
	public void setElementRelationConfigure(HAPConfigureParentRelationComplex relationConfigure) {    this.setAttributeValueObject(ATTR_ELEMENTRELATIONCONFIGURE, relationConfigure);      }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexContainer out = new HAPDefinitionEntityComplexContainer();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
