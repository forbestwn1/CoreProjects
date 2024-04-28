package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public abstract class HAPDefinitionDataAssociation extends HAPEntityInfoImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String DIRECTION = "direction";

	private String m_direction;

	private String m_type;
	
	private String m_baseEntityIdPath;
	
	public HAPDefinitionDataAssociation(String type) {
		this.m_type = type;
	}
	
	public String getType() {  return this.m_type;    }

	public String getDirection() {    return this.m_direction;    }
	public void setDirection(String direction) {    this.m_direction = direction;      }

	public String getBaseEntityIdPath() {    return this.m_baseEntityIdPath;     }
	public void setBaseEntityIdPath(String idPath) {    this.m_baseEntityIdPath = idPath;     }
	
	public abstract HAPDefinitionDataAssociation cloneDataAssocation();

	protected void cloneToDataAssociation(HAPDefinitionDataAssociation dataAssociation) {
		this.cloneToEntityInfo(dataAssociation);
		dataAssociation.m_type = this.m_type;
		dataAssociation.m_direction = this.m_direction;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(DIRECTION, this.getDirection());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject daJsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(daJsonObj);
		
		this.m_direction = (String)daJsonObj.opt(HAPDefinitionDataAssociation.DIRECTION);
		if(HAPUtilityBasic.isStringEmpty(this.m_direction)) {
			this.m_direction = HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM;
		}
		
		return true;
	}
}
