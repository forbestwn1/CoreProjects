package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;

public class HAPManualDefinitionBrickContainerBase extends HAPManualDefinitionBrick{

	private HAPIdBrickType m_childBrickTypeId;
	
	public HAPManualDefinitionBrickContainerBase(HAPIdBrickType brickTypeId, HAPIdBrickType childBrickTypeId) {
		super(brickTypeId);
		this.setAttributeIndex(0);
		this.m_childBrickTypeId = childBrickTypeId;
	}
	
	public HAPManualDefinitionBrickContainerBase (HAPIdBrickType brickTypeId) {
		this(brickTypeId, null);
	}
	
	public String addElementWithAttribute(HAPManualDefinitionAttributeInBrick attr) {
		if(HAPUtilityBasic.isStringEmpty(attr.getName())) {
			attr.setName(this.generateAttributeName());
		}
		this.setAttribute(attr);
		return attr.getName();
	}
	
	public String addElementWithBrickOrReference(HAPEntityOrReference brickOrRef) {
		return this.addElementAnom(brickOrRef);
	}
	
	public String addElementWithBrick(HAPManualDefinitionBrick brick) {
		String out = null;
		if(brick instanceof HAPEntityInfo) {
			HAPManualDefinitionAttributeInBrick attr = new HAPManualDefinitionAttributeInBrick(((HAPEntityInfo)brick).getName(), new HAPManualDefinitionWrapperValueBrick(brick));
			out = this.addElementWithAttribute(attr);
		}
		else {
			out = this.addElementWithBrickOrReference(brick);
		}
		return out;
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
