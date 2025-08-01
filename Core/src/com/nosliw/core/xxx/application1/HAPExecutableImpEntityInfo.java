package com.nosliw.core.xxx.application1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;

abstract public class HAPExecutableImpEntityInfo extends HAPExecutableImp  implements HAPEntityInfoWritable{
	
	protected String m_id;
	
	//name
	protected String m_name;

	protected String m_status;
	
	//name
	protected String m_displayName;

	//description
	protected String m_description;
	
	protected HAPInfo m_info;

	public HAPExecutableImpEntityInfo() {}
	
	public HAPExecutableImpEntityInfo(HAPEntityInfo entityInfo) {
		this.m_name = entityInfo.getName();
		this.m_displayName = entityInfo.getDisplayName();
		this.m_description = entityInfo.getDescription();
		this.m_info = entityInfo.getInfo();
	}
	
	@Override
	public String getId() {   return this.m_id;   }
	@Override
	public void setId(String id) {    this.m_id = id;   }
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }
	@Override
	public void setInfo(HAPInfo info) {   this.m_info = info;    }
	
	@Override
	public String getName() {  return this.m_name;  }
	@Override
	public void setName(String name) {   this.m_name = name;    }

	@Override
	public String getStatus() {  return this.m_status;  }
	@Override
	public void setStatus(String status) {   this.m_status = status;    }

	@Override
	public String getDisplayName() {  return this.m_displayName;  }
	@Override
	public void setDisplayName(String name) {   this.m_displayName = name;    }
	
	@Override
	public String getDescription() {   return this.m_description;  }
	@Override
	public void setDescription(String description) {    this.m_description = description;      }

	@Override
	public HAPEntityInfoImp cloneEntityInfo() {
		HAPEntityInfoImp out = new HAPEntityInfoImp();
		out.buildEntityInfoByJson(this.toStringValue(HAPSerializationFormat.JSON));
		return out;
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {
		HAPUtilityEntityInfo.cloneTo(this, entityInfo);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);
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
		this.m_displayName = jsonObj.optString(DISPLAYNAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
	}
}
