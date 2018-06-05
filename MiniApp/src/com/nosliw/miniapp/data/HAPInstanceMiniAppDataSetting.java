package com.nosliw.miniapp.data;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;

public class HAPInstanceMiniAppDataSetting extends HAPInstanceMiniAppData{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String STATUS = "status";
	
	@HAPAttribute
	public static final String VERSION = "version";
	
	private String m_status;
	
	private String m_version;
	
	private String m_id;
	
	@Override
	public String getType() {  return HAPConstant.MINIAPPDATA_TYPE_SETTING;  }

	public String getStatus() {  return this.m_status;  }
	public void setStatus(String status) {  this.m_status = status;   }

	public String getVersion() {  return this.m_version;   }
	public void setVersion(String version) {  this.m_version = version;   }
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(STATUS, this.m_status);
		jsonMap.put(VERSION, this.m_version);
	}
	

	@Override
	protected boolean buildObjectByFullJson(Object json){
		super.buildObjectByFullJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_status = (String)jsonObj.opt(STATUS);
		this.m_version = (String)jsonObj.opt(VERSION);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}
