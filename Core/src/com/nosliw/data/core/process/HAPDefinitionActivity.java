package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPDefinitionActivity extends HAPEntityInfoImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";
	
	//unique id for each step
	private String m_id;

	public HAPDefinitionActivity() {
	}

	abstract public String getType();

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_id = jsonObj.getString(ID);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
