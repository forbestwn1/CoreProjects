package com.nosliw.miniapp.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

public class HAPMiniAppTaskDataSource extends HAPDefinitionMiniAppService{

	@HAPAttribute
	public static final String DATASOURCEID = "dataSourceId";
	
	private String m_dataSourceId;
	
	@Override
	public String getType() { return HAPConstant.MINIAPPSERVICE_TYPE_DATASOURCE;  }
	
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(DATASOURCEID, this.m_dataSourceId);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_dataSourceId = (String)jsonObj.opt(DATASOURCEID);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}
	
}
