package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

public class HAPEntityInfoImp extends HAPSerializableImp implements HAPEntityInfo{

	private String m_id;
	
	//name
	private String m_name;

	//name
	private String m_displayName;

	//description
	private String m_description;
	
	private HAPInfo m_info;

	public HAPEntityInfoImp() {
		this.m_info = new HAPInfoImpSimple(); 
	}
	
	public HAPEntityInfoImp(String name, String description) {
		this.m_info = new HAPInfoImpSimple(); 
		this.m_name = name;
		this.m_description = description;
	}

	@Override
	public void setId(String id) {
		this.m_id = id;
		if(this.m_name==null)   this.m_name = id;
	}

	@Override
	public String getId() {    return this.m_id;    }
	
	@Override
	public String getName() {  return this.m_name;   }

	@Override
	public void setName(String name) {  
		this.m_name = name;
		if(this.m_id==null)   this.m_id = name;
	}

	@Override
	public String getDisplayName() {  
		if(this.m_displayName!=null) return this.m_displayName;
		if(this.m_name!=null)   return this.m_name;
		return this.m_id;
	}

	@Override
	public void setDisplayName(String name) {  this.m_displayName = name;  }

	@Override
	public String getDescription() {   return this.m_description;   }
	
	@Override
	public void setDescription(String description) {   this.m_description = description;   }

	@Override
	public HAPInfo getInfo() {  return this.m_info;  }

	public Object getInfoValue(String name) {  return this.m_info.getValue(name);   }

	@Override
	public void setInfo(HAPInfo info) {
		if(info!=null)		this.m_info = info.cloneInfo();  
	}
	
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
		this.setId((String)jsonObj.opt(ID));
		this.setName((String)jsonObj.opt(NAME));
		this.setDisplayName((String)jsonObj.opt(DISPLAYNAME));
		this.setDescription(jsonObj.optString(DESCRIPTION));
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPEntityInfoImp) {
			HAPEntityInfoImp infoEntity = (HAPEntityInfoImp)obj;
			if(!HAPBasicUtility.isEquals(infoEntity.m_id, this.m_id))   return false;
			if(!HAPBasicUtility.isEquals(infoEntity.m_name, this.m_name))   return false;
			if(!HAPBasicUtility.isEquals(infoEntity.m_displayName, this.m_displayName))   return false;
			if(!HAPBasicUtility.isEquals(infoEntity.m_description, this.m_description))   return false;;
			if(!HAPBasicUtility.isEquals(infoEntity.m_info, this.m_info))  return false;
			return true;
		}
		return false;
	}
}
