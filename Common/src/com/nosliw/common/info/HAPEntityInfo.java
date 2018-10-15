package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPEntityInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String INFO = "info";
	
	//name, for display
	private String m_name;

	//description
	private String m_description;
	
	private HAPInfo m_info;

	public HAPEntityInfo() {
		this.m_info = new HAPInfoImpSimple(); 
	}
	public HAPEntityInfo(String name, String description) {
		this.m_info = new HAPInfoImpSimple(); 
		this.m_name = name;
		this.m_description = description;
	}
	
	public HAPInfo getInfo() {  return this.m_info;  }
	public Object getInfoValue(String name) {  return this.m_info.getValue(name);   }
	public void setInfo(HAPInfo info) {  this.m_info = info;  }
	
	public String getName() {  return this.m_name;   }
	public String getDescription() {   return this.m_description;   }
	public void setName(String name) {  this.m_name = name;    }
	public void setDescription(String description) {   this.m_description = description;   }

	public HAPEntityInfo clone() {
		HAPEntityInfo out = new HAPEntityInfo(this.m_name, this.m_description);
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(DESCRIPTION, this.m_description);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
		return true;  
	}
}
