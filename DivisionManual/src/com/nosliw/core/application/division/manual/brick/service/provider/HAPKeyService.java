package com.nosliw.core.application.division.manual.brick.service.provider;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPKeyService extends HAPSerializableImp{

	public static final String ATTR_ID = "id";

	private String m_id;
	
	public HAPKeyService() {
		
	}
	
	public String getServiceId() {    return this.m_id;    }
	
	public void setServiceId(String id) {    this.m_id = id;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_id = objJson.getString(ATTR_ID);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
