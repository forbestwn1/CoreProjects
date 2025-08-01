package com.nosliw.core.application.division.manual.brick.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.xxx.application1.brick.container.HAPBrickContainerList;

public class HAPManualBrickContainerList extends HAPManualBrickContainer implements HAPBrickContainerList{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBrickContainerList.ATTRSORT, new ArrayList<String>());
	}
	
	public List<String> getSort(){   return (List<String>)this.getAttributeValueOfValue(HAPBrickContainerList.ATTRSORT);     }
	
	@Override
	public List<HAPAttributeInBrick> getElements(){
		List<HAPAttributeInBrick> out = new ArrayList<HAPAttributeInBrick>();
		List<String> sorted = this.getSort();
		for(String attr : sorted) {
			out.add(this.getAttribute(attr));
		}
		return out;
	}
	 
}
