package com.nosliw.data.core.domain.entity.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityContainerComplex extends HAPManualBrickComplex{

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

	public String addElementAttribute(String eleId, HAPIdEntityInDomain entityId, Boolean autoProcess) {
		this.setAttribute(eleId, new HAPEmbededDefinition(entityId), this.getElmentValueTypeInfo(), autoProcess);
		return eleId;
	}

	public HAPInfoBrickType getElmentValueTypeInfo() {     return (HAPInfoBrickType)this.getAttributeValueOfValue(ATTR_ELEMENT_TYPEINFO);     }
	public void setElementValueTypeInfo(HAPInfoBrickType eleValueTypeInfo) {    this.setAttributeValueObject(ATTR_ELEMENT_TYPEINFO, eleValueTypeInfo);      }
	
	public HAPConfigureParentRelationComplex getElementRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(ATTR_ELEMENTRELATIONCONFIGURE, new HAPConfigureParentRelationComplex());     }
	public void setElementRelationConfigure(HAPConfigureParentRelationComplex relationConfigure) {    this.setAttributeValueObject(ATTR_ELEMENTRELATIONCONFIGURE, relationConfigure);      }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityContainerComplex out = new HAPDefinitionEntityContainerComplex();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
