package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociationGroup;

public class HAPServiceActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	private String m_provider;
	
	//parms path
	private HAPDefinitionDataAssociationGroup m_parmMapping;
	
	public HAPServiceActivityDefinition(String type) {
		super(type);
	}

	public String getProvider() {   return this.m_provider;  }
	
	public HAPDefinitionDataAssociationGroup getParmMapping() {  return this.m_parmMapping;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_provider = jsonObj.optString(PROVIDER);
		this.m_parmMapping = new HAPDefinitionDataAssociationGroup();
		this.m_parmMapping.buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
		jsonMap.put(PARMMAPPING, HAPJsonUtility.buildJson(this.m_parmMapping, HAPSerializationFormat.JSON));
	}
}
