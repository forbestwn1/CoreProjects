package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public abstract class HAPDynamicDefinitionItemLeaf extends HAPDynamicDefinitionItem{

	@HAPAttribute
	public final static String CRITERIA = "criteria"; 
	
	private HAPDynamicDefinitionCriteria m_criteria;
	
	public HAPDynamicDefinitionItemLeaf() {}
	
	public HAPDynamicDefinitionItemLeaf(HAPDynamicDefinitionCriteria criteria) {
		this.m_criteria= criteria;
	}

	public HAPDynamicDefinitionCriteria getCriteria() {	return this.m_criteria;	}
	public void setCriteria(HAPDynamicDefinitionCriteria criteria) {    this.m_criteria = criteria;     }

	@Override
	public HAPDynamicDefinitionItem getChild(String childName) {   return null;    }

	public static void parseToDynamicLeafInfo(HAPDynamicDefinitionItemLeaf dynamicLeafInfo, JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionItem.parseToDynamicInfo(dynamicLeafInfo, jsonObj);
		dynamicLeafInfo.setCriteria(HAPDynamicDefinitionCriteria.parseDynamicCriteria(jsonObj.getJSONObject(CRITERIA), dataRuleMan));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CRITERIA, this.m_criteria.toStringValue(HAPSerializationFormat.JSON));
	}
}
