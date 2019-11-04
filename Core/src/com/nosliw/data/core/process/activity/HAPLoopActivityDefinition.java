package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivityTask;

public class HAPLoopActivityDefinition extends HAPDefinitionActivityTask{

	@HAPAttribute
	public static String DATANAME = "dataName";

	@HAPAttribute
	public static String INDEXNAME = "indexName";

	private String m_dataName;

	private String m_indexName;

	public HAPLoopActivityDefinition(String type) {
		super(type);
	}
	
	public String getDataName(){  return this.m_dataName;    }
	public String getIndexName(){  return this.m_indexName;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_dataName = jsonObj.optString(DATANAME);
		this.m_indexName = jsonObj.optString(INDEXNAME);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATANAME, this.m_dataName);
		jsonMap.put(INDEXNAME, this.m_indexName);
	}
}
