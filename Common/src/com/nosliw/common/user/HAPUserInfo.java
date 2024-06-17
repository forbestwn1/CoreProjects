package com.nosliw.common.user;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;

@HAPEntityWithAttribute
public class HAPUserInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String USER = "user";

	@HAPAttribute
	public static String ENTITIES = "entities";

	private HAPUser m_user;
	
	private Map<String, Object> m_relatedEntities;
	
	public HAPUserInfo() {
		this.m_relatedEntities = new LinkedHashMap<String, Object>();
	}
	
	public HAPUser getUser() {  return this.m_user;   }
	public void setUser(HAPUser user) {   this.m_user = user;    }
	
	public void setRelatedEntity(String name, Object entity) {    this.m_relatedEntities.put(name, entity);    }
	public Object getRelatedEntity(String name) {      return this.m_relatedEntities.get(name);      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(USER, HAPUtilityJson.buildJson(m_user, HAPSerializationFormat.JSON));
		
		Map<String, String> entitiesMap = new LinkedHashMap<String, String>();
		for(String name : this.m_relatedEntities.keySet()) {
			entitiesMap.put(name, HAPUtilityJson.buildJson(this.m_relatedEntities.get(name), HAPSerializationFormat.JSON));
		}
		jsonMap.put(ENTITIES, HAPUtilityJson.buildMapJson(entitiesMap));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject userJsonObj = jsonObj.optJSONObject(USER);
		if(userJsonObj!=null) {
			this.m_user = (HAPUser)HAPManagerSerialize.getInstance().buildObject(HAPUser.class.getName(), userJsonObj, HAPSerializationFormat.JSON);
		}
		return true;
	}

}
