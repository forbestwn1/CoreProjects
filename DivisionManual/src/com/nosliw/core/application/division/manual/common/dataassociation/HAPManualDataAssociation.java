package com.nosliw.core.application.division.manual.common.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public abstract class HAPManualDataAssociation extends HAPEntityInfoImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static final String DIRECTION = "direction";

	private String m_type;
	
	private String m_baseEntityIdPath;
	
	private String m_direction;

	public HAPManualDataAssociation(String type) {
		this.m_type = type;
		this.m_direction = HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM;
	}
	
	public String getType() {  return this.m_type;    }

	public String getBaseEntityIdPath() {    return this.m_baseEntityIdPath;     }
	public void setBaseEntityIdPath(String idPath) {    this.m_baseEntityIdPath = idPath;     }

	public String getDirection() {    return this.m_direction;    }
	public void setDirection(String direction) {    this.m_direction = direction;      }


	public abstract HAPManualDataAssociation cloneDataAssocation();

	protected void cloneToDataAssociation(HAPManualDataAssociation dataAssociation) {
		this.cloneToEntityInfo(dataAssociation);
		dataAssociation.m_type = this.m_type;
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
		Object dirObj = daJsonObj.opt(HAPDefinitionDataAssociation.DIRECTION);
		if(dirObj!=null) {
			this.m_direction = (String)dirObj;
		}
		return true;
	}
}
