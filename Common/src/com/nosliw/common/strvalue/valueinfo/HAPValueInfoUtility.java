package com.nosliw.common.strvalue.valueinfo;

import java.util.Map;

import org.w3c.dom.Element;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPValueInfoUtility {

	public static HAPStringableValueEntity validateStringableValueEntity(HAPValueInfoEntity entityValueInfo, HAPStringableValueEntity entity){
		HAPStringableValueEntity out = null;
		boolean isMandatory = entityValueInfo.getAtomicAncestorValueBoolean(HAPValueInfoEntity.MANDATORY);
		if(!isMandatory && entity.isEmpty())   return null;
		return entity;
	}
	
}
