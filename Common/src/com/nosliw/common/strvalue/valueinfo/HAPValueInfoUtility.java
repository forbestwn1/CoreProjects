package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;

public class HAPValueInfoUtility {

	public static HAPStringableValueEntity validateStringableValueEntity(HAPValueInfoEntity entityValueInfo, HAPStringableValueEntity entity){
		HAPStringableValueEntity out = null;
		boolean isMandatory = entityValueInfo.getBasicAncestorValueBoolean(HAPValueInfoEntity.ENTITY_PROPERTY_MANDATORY);
		if(!isMandatory && entity.isEmpty())   return null;
		return entity;
	}
	
	
}
