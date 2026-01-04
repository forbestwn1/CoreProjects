package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public abstract class HAPDynamicDefinitionItem extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public abstract HAPDynamicDefinitionItem getChild(String childName);
	
	public static void parseToDynamicInfo(HAPDynamicDefinitionItem dynamicInfo, JSONObject jsonObj) {
		dynamicInfo.buildEntityInfoByJson(jsonObj);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}

	
	public static HAPDynamicDefinitionItem parse(Object obj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionItem out = null;
		
		JSONObject jsonObj = (JSONObject)obj;
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SET:
			out = HAPDynamicDefinitionItemSet.parseSet(jsonObj, dataRuleMan); 
			break;
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SINGLE:
			out = HAPDynamicDefinitionItemSingle.parseSimple(jsonObj, dataRuleMan); 
			break;
		case HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_NODE:
			out = HAPDynamicDefinitionItemNode.parseNode(jsonObj, dataRuleMan);
			break;
		}
		
		if(out.getName()==null) {
			out.setName(HAPConstantShared.NAME_DEFAULT);
		}
		
		return out;
	}
	
}
