package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPManualEntitySimple;

public class HAPDefinitionEntityContainer<T extends HAPManualEntity> extends HAPManualEntitySimple{

	protected HAPDefinitionEntityContainer (HAPIdBrickType entityType) {
		super(entityType);
	}
	
	public String addElement(T element) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttributeEntity(attrName, element);
		return attrName;
	}

	
}
