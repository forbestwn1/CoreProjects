package com.nosliw.core.application.division.manual.common.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPManualDataAssociation extends HAPEntityInfoImp{

	@HAPAttribute
	public static String TYPE = "type";

	private String m_type;
	
	private String m_baseEntityIdPath;
	
	public HAPManualDataAssociation(String type) {
		this.m_type = type;
	}
	
	public String getType() {  return this.m_type;    }

	public String getBaseEntityIdPath() {    return this.m_baseEntityIdPath;     }
	public void setBaseEntityIdPath(String idPath) {    this.m_baseEntityIdPath = idPath;     }
	
	public abstract HAPManualDataAssociation cloneDataAssocation();

	protected void cloneToDataAssociation(HAPManualDataAssociation dataAssociation) {
		this.cloneToEntityInfo(dataAssociation);
		dataAssociation.m_type = this.m_type;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject daJsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(daJsonObj);
		return true;
	}
}
