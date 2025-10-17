package com.nosliw.core.application.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPInfoDynamicLeafSimple extends HAPInfoDynamicLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICTASK_INFO_TYPE_SIMPLE;
	}
	
	public static HAPInfoDynamicLeafSimple parseSimple(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInfoDynamicLeafSimple out = new HAPInfoDynamicLeafSimple();
		HAPInfoDynamicLeaf.parseToDynamicLeafInfo(out, jsonObj, dataRuleMan);
		return out;
	}
}
