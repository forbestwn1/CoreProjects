package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.dynamiccriteria.HAPCriteriaDynamic;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamicLeaf extends HAPInfoDynamic{

	@HAPAttribute
	public final static String CRITERIA = "criteria"; 
	
	private HAPCriteriaDynamic m_criteria;
	
	public HAPInfoDynamicLeaf() {}
	
	public HAPInfoDynamicLeaf(HAPCriteriaDynamic criteria) {
		this.m_criteria= criteria;
	}

	public HAPCriteriaDynamic getCriteria() {	return this.m_criteria;	}
	public void setCriteria(HAPCriteriaDynamic criteria) {    this.m_criteria = criteria;     }

	@Override
	public HAPInfoDynamic getChild(String childName) {   return null;    }

	public static void parseToDynamicLeafInfo(HAPInfoDynamicLeaf dynamicLeafInfo, JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInfoDynamic.parseToDynamicInfo(dynamicLeafInfo, jsonObj);
		dynamicLeafInfo.setCriteria(HAPCriteriaDynamic.parseDynamicCriteria(jsonObj.getJSONObject(CRITERIA), dataRuleMan));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CRITERIA, this.m_criteria.toStringValue(HAPSerializationFormat.JSON));
	}
}
