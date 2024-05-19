package com.nosliw.core.application.brick.service.provider;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPKeyService extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";

	private String m_id;
	
	public HAPKeyService() {
		
	}
	
	public String getServiceId() {    return this.m_id;    }
	
	public void setServiceId(String id) {    this.m_id = id;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_id = objJson.getString(ID);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
