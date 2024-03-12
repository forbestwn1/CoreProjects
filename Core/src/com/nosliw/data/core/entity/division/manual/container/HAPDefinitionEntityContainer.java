package com.nosliw.data.core.entity.division.manual.container;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntitySimple;

public class HAPDefinitionEntityContainer<T extends HAPManualEntity> extends HAPManualEntitySimple{

	protected HAPDefinitionEntityContainer (HAPIdEntityType entityType) {
		super(entityType);
	}
	
	public String addElement(T element) {
		String attrName = HAPConstantShared.PREFIX_ELEMENTID_COTAINER+this.generateId();
		this.setAttributeEntity(attrName, element);
		return attrName;
	}

	
}
