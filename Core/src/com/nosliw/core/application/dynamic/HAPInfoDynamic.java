package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamic extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public abstract HAPInfoDynamic getChild(String childName);
	
	public static void parseToDynamicInfo(HAPInfoDynamic dynamicInfo, JSONObject jsonObj) {
		dynamicInfo.buildEntityInfoByJson(jsonObj);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}

	
	public static HAPInfoDynamic parse(Object obj, HAPManagerDataRule dataRuleMan) {
		HAPInfoDynamic out = null;
		
		JSONObject jsonObj = (JSONObject)obj;
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SET:
			out = HAPInfoDynamicLeafSet.parseSet(jsonObj, dataRuleMan); 
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SIMPLE:
			out = HAPInfoDynamicLeafSimple.parseSimple(jsonObj, dataRuleMan); 
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_NODE:
			out = HAPInfoDynamicNode.parseNode(jsonObj, dataRuleMan);
			break;
		}
		
		if(out.getName()==null) {
			out.setName(HAPConstantShared.NAME_DEFAULT);
		}
		
		return out;
	}
	
}
