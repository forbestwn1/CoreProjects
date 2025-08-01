package com.nosliw.core.application.division.manual.brick.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickWithEntityInfo;
import com.nosliw.core.xxx.application1.brick.container.HAPBrickContainer;

public class HAPManualBrickContainer extends HAPManualBrickWithEntityInfo implements HAPBrickContainer{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public List<HAPAttributeInBrick> getElements(){
		List<HAPAttributeInBrick> out = new ArrayList<HAPAttributeInBrick>();
		for(HAPAttributeInBrick attr : this.getAttributes()) {
			if(HAPUtilityNosliw.getNosliwCoreName(attr.getName())==null) {
				out.add(attr);
			}
		}
		return out;
	}
	 
}
