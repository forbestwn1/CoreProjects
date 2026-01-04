package com.nosliw.core.application.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionItemSet extends HAPDynamicDefinitionItemLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SET;
	}
	
	public static HAPDynamicDefinitionItemSet parseSet(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionItemSet out = new HAPDynamicDefinitionItemSet();
		HAPDynamicDefinitionItemLeaf.parseToDynamicLeafInfo(out, jsonObj, dataRuleMan);
		return out;
	}
	
}
