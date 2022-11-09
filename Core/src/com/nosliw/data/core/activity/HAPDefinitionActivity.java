package com.nosliw.data.core.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.task.HAPDefinitionTaskImp;

@HAPEntityWithAttribute
public abstract class HAPDefinitionActivity extends HAPDefinitionTaskImp{

	@HAPAttribute
	public static String ACTIVITYTYPE = "activityType";

	@HAPAttribute
	public static String COFIGURATION = "configuration";

	private String m_activityType;
	
	public HAPDefinitionActivity(String type) {
		super(HAPConstantShared.TASK_TYPE_ACTIVITY);
		this.m_activityType = type;
	}

	public String getActivityType() {   return this.m_activityType;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ACTIVITYTYPE, this.getActivityType());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void cloneToActivityDefinition(HAPDefinitionActivity activity) {
		this.cloneToEntityInfo(activity);
		activity.m_activityType = this.m_activityType;
	}
	
	public abstract HAPDefinitionActivity cloneActivityDefinition();
	
	public abstract void parseActivityDefinition(Object obj, HAPDefinitionEntityInDomainComplex complexEntity, HAPSerializationFormat format);
	
	protected JSONObject getConfigurationObject(JSONObject jsonObj) {
		JSONObject out = jsonObj.optJSONObject(COFIGURATION);
		if(out==null) {
			out = new JSONObject();
		}
		return out;
	}
}
