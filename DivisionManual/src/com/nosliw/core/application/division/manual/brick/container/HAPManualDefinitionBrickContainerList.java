package com.nosliw.core.application.division.manual.brick.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.container.HAPBrickContainerList;

public class HAPManualDefinitionBrickContainerList extends HAPManualDefinitionBrickContainerBase{

	public HAPManualDefinitionBrickContainerList() {
		super(HAPEnumBrickType.CONTAINERLIST_100);
	}

	@Override
	public void init() {
		this.setAttributeValueWithValue(HAPBrickContainerList.ATTRSORT, new ArrayList<String>());
	}
	
	public List<String> getSort(){   return (List<String>)this.getAttributeValueOfValue(HAPBrickContainerList.ATTRSORT);     }
	
	@Override
	public String addElementWithAttribute(HAPManualDefinitionAttributeInBrick attr) {
		String eleId = super.addElementWithAttribute(attr);
		this.getSort().add(eleId);
		return eleId;
	}
	
	@Override
	public String addElementWithBrickOrReference(HAPEntityOrReference brickOrRef) {
		String eleId = super.addElementWithBrickOrReference(brickOrRef);
		this.getSort().add(eleId);
		return eleId;
	}

}
