package com.nosliw.core.application.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionItemSingle extends HAPDynamicDefinitionItemLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SINGLE;
	}
	
	public static HAPDynamicDefinitionItemSingle parseSimple(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionItemSingle out = new HAPDynamicDefinitionItemSingle();
		HAPDynamicDefinitionItemLeaf.parseToDynamicLeafInfo(out, jsonObj, dataRuleMan);
		return out;
	}
}
