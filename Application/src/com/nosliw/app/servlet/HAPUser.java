package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPUser extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String GROUPS = "groups";
	
	private String m_id;

	private String m_name;

	private List<HAPGroup> m_groups;
	
	public HAPUser() {
		this.m_groups = new ArrayList<HAPGroup>();
	}
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;   }

	public String getName() {  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;   }

	public void addGroup(HAPGroup group) {   this.m_groups.add(group);   }
	public void addGroups(List<HAPGroup> groups) {  this.m_groups.addAll(groups);   }
	public List<HAPGroup> getGroups(){   return this.m_groups;   }
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(GROUPS, HAPJsonUtility.buildJson(this.m_groups, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_name = jsonObj.optString(NAME);
		this.m_groups = HAPSerializeUtility.buildListFromJsonArray(HAPGroup.class.getName(), jsonObj.optJSONArray(GROUPS));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
