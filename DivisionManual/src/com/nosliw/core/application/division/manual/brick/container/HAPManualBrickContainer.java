package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;

public class HAPManualBrickContainer extends HAPManualBrickBlockSimple{

	public HAPManualBrickContainer () {
		super(HAPEnumBrickType.CONTAINER_100);
		this.setAttributeIndex(0);
	}
	
	public String addElement(HAPManualAttribute attr) {
		if(HAPUtilityBasic.isStringEmpty(attr.getName())) {
			attr.setName(this.generateAttributeName());
		}
		this.setAttribute(attr);
		return attr.getName();
	}
	
	public String addElement(HAPEntityOrReference brickOrRef) {
		String attrName = null;
		String brickOrRefType = brickOrRef.getEntityOrReferenceType();
		if(brickOrRefType.equals(HAPConstantShared.BRICK)) {
			if(brickOrRef instanceof HAPEntityInfo) {
				attrName = ((HAPEntityInfo)brickOrRef).getId();
				this.setAttributeWithValueBrick(attrName, brickOrRef);
			}
			else {
				attrName = this.addElementAnom(brickOrRef);
			}
		}
		else if(brickOrRefType.equals(HAPConstantShared.RESOURCEID)) {
			attrName = this.addElementAnom(brickOrRef);
		}
		return attrName;
	}
	
	private String addElementAnom(HAPEntityOrReference brickOrRef) {
		String attrName = this.generateAttributeName();
		this.setAttributeWithValueBrick(attrName, brickOrRef);
		return attrName;
	}
	
	private String generateAttributeName() {
		Integer index = (Integer)this.getAttributeValueWithValue(HAPBrickContainer.ATTRINDEX);
		index++;
		this.setAttributeWithValueValue(HAPBrickContainer.ATTRINDEX, index);
		return HAPConstantShared.PREFIX_ELEMENTID_COTAINER+index+"";
	}
	
	private void setAttributeIndex(Integer index) {		this.setAttributeWithValueValue(HAPBrickContainer.ATTRINDEX, index);	}

}
