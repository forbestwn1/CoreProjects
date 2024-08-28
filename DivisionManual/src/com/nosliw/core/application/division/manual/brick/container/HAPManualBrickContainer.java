package com.nosliw.core.application.division.manual.brick.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickWithEntityInfo;

public class HAPManualBrickContainer extends HAPManualBrickWithEntityInfo implements HAPBrickContainer{

	@Override
	public void init() {
		super.init();
		this.setAttributeIndex(0);
	}
	
	@Override
	public List<HAPAttributeInBrick> getElements(){
		List<HAPAttributeInBrick> out = new ArrayList<HAPAttributeInBrick>();

		for(HAPAttributeInBrick attr : this.getAttributes()) {
			if(!ATTRINDEX.equals(attr.getName())) {
				out.add(attr);
			}
		}
		
		return out;
	}
	 
	public String addElement(HAPEntityOrReference brickOrRef) {
		String attrName = null;
		String brickOrRefType = brickOrRef.getEntityOrReferenceType();
		if(brickOrRefType.equals(HAPConstantShared.BRICK)) {
			if(brickOrRef instanceof HAPEntityInfo) {
				attrName = ((HAPEntityInfo)brickOrRef).getId();
				this.setAttributeValueWithBrick(attrName, brickOrRef);
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
		Integer index = (Integer)this.getAttributeValueOfValue(ATTRINDEX);
		index++;
		this.setAttributeValueWithValue(ATTRINDEX, index);
		String attrName = index+"";
		this.setAttributeValueWithBrick(attrName, brickOrRef);
		return attrName;
	}
	
	private void setAttributeIndex(Integer index) {		this.setAttributeValueWithValue(ATTRINDEX, index);	}
}
