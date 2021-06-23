package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableActivity extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String CATEGARY = "categary";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String DEFINITION = "definition";

	private String m_categary;

	private String m_type;
	
	private String m_id;
	
//	private HAPDefinitionActivity m_activityDefinition;

	public HAPExecutableActivity() {}
	
	public HAPExecutableActivity(String categary, String id, HAPDefinitionActivity activityDef) {
		super(activityDef);
		this.m_categary = categary;
		this.m_id = id;
//		this.m_activityDefinition = activityDef;
		this.m_type = activityDef.getType();
	}
	
	public String getType() {   return this.m_type;  }
	public String getCategary() {   return this.m_categary;   } 
	public String getId() {  return this.m_id;  }
	
//	public HAPDefinitionActivity getActivityDefinition() {   return this.m_activityDefinition;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.getString(ID);
		this.m_categary = jsonObj.getString(CATEGARY);
		this.m_type = jsonObj.getString(TYPE);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(TYPE, this.m_type);
//		jsonMap.put(DEFINITION, this.m_activityDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
}
