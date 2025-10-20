package com.nosliw.core.application.entity.datarule;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.common.brick.HAPBrickImp;

public class HAPBrickContainerImp extends HAPBrickImp implements HAPBrickContainer{

	public HAPBrickContainerImp() {
		this.setAttributeIndex(0);
	}
	
	@Override
	public List<HAPAttributeInBrick> getElements() {
		List<HAPAttributeInBrick> out = new ArrayList<HAPAttributeInBrick>();
		for(HAPAttributeInBrick attr : this.getAttributes()) {
			if(HAPUtilityNosliw.getNosliwCoreName(attr.getName())==null) {
				out.add(attr);
			}
		}
		return out;
	}

	public String addElementWrapper(HAPWrapperValue valueWrapper) {
		String attrName = this.generateAttributeName();
		this.setAttribute(new HAPAttributeInBrick(attrName, valueWrapper));
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
