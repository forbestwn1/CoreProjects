package com.nosliw.miniapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPUser extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String GROUPS = "groups";
	
	private String m_id;

	private List<String> m_groups;
	
	public HAPUser() {
		this.m_groups = new ArrayList<String>();
	}
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;   }

	public void addGroup(String group) {   this.m_groups.add(group);   }
	public void addGroups(List<String> groups) {  this.m_groups.addAll(groups);   }
	public List<String> getGroups(){   return this.m_groups;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(GROUPS, HAPJsonUtility.buildJson(this.m_groups, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildEntityInfoByJson(jsonObj);
		this.m_id = jsonObj.optString(ID);
		this.m_groups = HAPSerializeUtility.buildListFromJsonArray(String.class.getName(), jsonObj.optJSONArray(GROUPS));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
