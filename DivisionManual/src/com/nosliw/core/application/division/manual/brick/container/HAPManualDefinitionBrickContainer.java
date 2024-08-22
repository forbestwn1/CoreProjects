package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;

public class HAPManualDefinitionBrickContainer extends HAPManualDefinitionBrick{

	private HAPIdBrickType m_childBrickTypeId;
	
	public HAPManualDefinitionBrickContainer(HAPIdBrickType childBrickTypeId) {
		super(HAPEnumBrickType.CONTAINER_100);
		this.setAttributeIndex(0);
		this.m_childBrickTypeId = childBrickTypeId;
	}
	
	public HAPManualDefinitionBrickContainer () {
		super(HAPEnumBrickType.CONTAINER_100);
		this.setAttributeIndex(0);
	}
	
	public String addElement(HAPManualDefinitionAttributeInBrick attr) {
		if(HAPUtilityBasic.isStringEmpty(attr.getName())) {
			attr.setName(this.generateAttributeName());
		}
		this.setAttribute(attr);
		return attr.getName();
	}
	
	public String addElement(HAPManualDefinitionWrapperValue valueWrapper) {
		String attrName = generateAttributeName();
		this.setAttributeWithValueWrapper(attrName, valueWrapper);
		return attrName;
	}
	
	public String addElement(HAPEntityOrReference brickOrRef) {
		return this.addElementAnom(brickOrRef);
	}
	
	private String addElementAnom(HAPEntityOrReference brickOrRef) {
		String attrName = this.generateAttributeName();
		this.setAttributeValueWithBrick(attrName, brickOrRef);
		return attrName;
	}
	
	private String generateAttributeName() {
		Integer index = (Integer)this.getAttributeValueOfValue(HAPBrickContainer.ATTRINDEX);
		index++;
		this.setAttributeValueWithValue(HAPBrickContainer.ATTRINDEX, index);
		return HAPConstantShared.PREFIX_ELEMENTID_COTAINER+index+"";
	}
	
	private void setAttributeIndex(Integer index) {		this.setAttributeValueWithValue(HAPBrickContainer.ATTRINDEX, index);	}

}
