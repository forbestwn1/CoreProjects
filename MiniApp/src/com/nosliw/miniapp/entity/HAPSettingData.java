package com.nosliw.miniapp.entity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPSettingData extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String OWNERTYPE = "ownerType";

	@HAPAttribute
	public static final String OWNERID = "ownerId";

	@HAPAttribute
	public static final String DATA = "data";
	
	private String m_id;
	
	private String m_name;
	
	private String m_ownerId;
	
	private String m_ownerType;
	
	private Object m_data;
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;  }

	public String getName() {  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;  }

	public String getOwnerId() {  return this.m_ownerId;  }
	public void setOwnerId(String ownerId) {  this.m_ownerId = ownerId;  }

	public String getOwnerType() {  return this.m_ownerType;  }
	public void setOwnerType(String ownerType) {  this.m_ownerType = ownerType;  }

	public Object getData() {   return this.m_data;   }
	public String getDataStr() { return this.m_data.toString();  }
	public void setData(Object data) {   this.m_data = data;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(OWNERID, this.m_ownerId);
		jsonMap.put(OWNERTYPE, this.m_ownerType);
		jsonMap.put(DATA, HAPJsonUtility.buildJson(this.m_data, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByFullJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_ownerId = (String)jsonObj.opt(OWNERID);
		this.m_ownerType = (String)jsonObj.opt(OWNERTYPE);
		this.m_data = jsonObj.optJSONObject(DATA);
		return true;
	}

	public static HAPSettingData buildObject(Object json) {
		HAPSettingData out = new HAPSettingData();
		out.buildObject(json, HAPSerializationFormat.JSON);
		return out;
	}
}
