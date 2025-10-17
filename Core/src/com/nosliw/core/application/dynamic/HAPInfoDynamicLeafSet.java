package com.nosliw.core.application.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPInfoDynamicLeafSet extends HAPInfoDynamicLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICTASK_INFO_TYPE_SET;
	}
	
	public static HAPInfoDynamicLeafSet parseSet(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInfoDynamicLeafSet out = new HAPInfoDynamicLeafSet();
		HAPInfoDynamicLeaf.parseToDynamicLeafInfo(out, jsonObj, dataRuleMan);
		return out;
	}
	
}
