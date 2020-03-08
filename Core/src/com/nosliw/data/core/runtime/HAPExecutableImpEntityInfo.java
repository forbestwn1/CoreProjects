package com.nosliw.data.core.runtime;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;

abstract public class HAPExecutableImpEntityInfo extends HAPExecutableImp  implements HAPEntityInfo{
	
	//name, for display
	protected String m_name;

	//description
	protected String m_description;
	
	protected HAPInfo m_info;

	public HAPExecutableImpEntityInfo(HAPEntityInfo entityInfo) {
		this.m_name = entityInfo.getName();
		this.m_description = entityInfo.getDescription();
		this.m_info = entityInfo.getInfo();
	}
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }
	public void setInfo(HAPInfo info) {   this.m_info = info;    }
	
	@Override
	public String getName() {  return this.m_name;  }
	public void setName(String name) {   this.m_name = name;    }

	@Override
	public String getDescription() {   return this.m_description;  }
	public void setDescription(String description) {    this.m_description = description;      }

	@Override
	public HAPEntityInfoImp cloneEntityInfo() {
		HAPEntityInfoImp out = new HAPEntityInfoImp();
		out.buildEntityInfoByJson(this.toStringValue(HAPSerializationFormat.JSON));
		return out;
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		HAPEntityInfoUtility.cloneTo(this, entityInfo);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPEntityInfoUtility.buildJsonMap(jsonMap, this);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildEntityInfoByJson(json);
		return true;  
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
	}
	
}
