package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBlockSimple;

public class HAPManualBrickContainer<T extends HAPManualBrick> extends HAPManualBlockSimple{

	protected HAPManualBrickContainer (HAPIdBrickType entityType) {
		super(entityType);
	}
	
	public String addElement(T element) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttributeWithValueBrick(attrName, element);
		return attrName;
	}

	
}