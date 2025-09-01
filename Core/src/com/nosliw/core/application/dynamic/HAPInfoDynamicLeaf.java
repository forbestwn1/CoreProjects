package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.bricktypecriteria.HAPCriteriaBrickType;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamicLeaf extends HAPInfoDynamic{

	@HAPAttribute
	public final static String CRITERIA = "criteria"; 
	
	private HAPCriteriaBrickType m_criteria;
	
	public HAPInfoDynamicLeaf() {}
	
	public HAPInfoDynamicLeaf(HAPCriteriaBrickType criteria) {
		this.m_criteria= criteria;
	}

	public HAPCriteriaBrickType getCriteria() {
		return this.m_criteria;
	}

	@Override
	public HAPInfoDynamic getChild(String childName) {   return null;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CRITERIA, this.m_criteria.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_criteria =  new HAPCriteriaBrickType();
		this.m_criteria.buildObject(jsonObj.getJSONObject(CRITERIA), HAPSerializationFormat.JSON);
		return true;
	}
}
