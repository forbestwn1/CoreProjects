package com.nosliw.miniapp.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPDefinitionServiceService extends HAPDefinitionService{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";
	
	private String m_serviceId;
	
	@Override
	public String getType() { return HAPConstant.MINIAPPSERVICE_TYPE_SERVICE;  }
	
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICEID, this.m_serviceId);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		super.buildObjectByFullJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_serviceId = (String)jsonObj.opt(SERVICEID);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}
	
}
