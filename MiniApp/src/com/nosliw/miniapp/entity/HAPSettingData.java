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
	public static final String OWNERINFO = "ownerInfo";

	@HAPAttribute
	public static final String DATA = "data";
	
	private String m_id;
	
	private HAPOwnerInfo m_ownerInfo;
	
	private String m_name;
	
	private Object m_data;
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;  }

	public String getName() {  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;  }

	public HAPOwnerInfo getOwnerInfo() {  return this.m_ownerInfo;  }
	public void setOwnerInfo(HAPOwnerInfo ownerInfo) {  this.m_ownerInfo = ownerInfo;  }

	public Object getData() {   return this.m_data;   }
	public String getDataStr() { return HAPJsonUtility.buildJson(this.m_data, HAPSerializationFormat.JSON);  }
	public void setData(Object data) {   this.m_data = data;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(OWNERINFO, HAPJsonUtility.buildJson(this.m_ownerInfo, HAPSerializationFormat.JSON));
		jsonMap.put(DATA, this.getDataStr());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_name = (String)jsonObj.opt(NAME);

		JSONObject ownerInfoJson = jsonObj.optJSONObject(OWNERINFO);
		if(ownerInfoJson!=null) {
			this.m_ownerInfo = new HAPOwnerInfo();
			this.m_ownerInfo.buildObject(ownerInfoJson, HAPSerializationFormat.JSON);
		}
		
		this.m_data = jsonObj.opt(DATA);
		return true;
	}

	public static HAPSettingData buildObject(Object json) {
		HAPSettingData out = new HAPSettingData();
		out.buildObject(json, HAPSerializationFormat.JSON);
		return out;
	}
}
