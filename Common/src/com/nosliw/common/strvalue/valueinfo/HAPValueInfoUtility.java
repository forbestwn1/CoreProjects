package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

public class HAPValueInfoUtility {

	public static HAPStringableValueEntity validateStringableValueEntity(HAPValueInfoEntity entityValueInfo, HAPStringableValueEntity entity){
		HAPStringableValueEntity out = null;
		boolean isMandatory = entityValueInfo.getAtomicAncestorValueBoolean(HAPValueInfoEntity.MANDATORY);
		if(!isMandatory && entity.isEmpty())   return null;
		return entity;
	}
	
	
}
