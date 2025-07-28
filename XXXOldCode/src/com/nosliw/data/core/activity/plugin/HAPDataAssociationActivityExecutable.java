package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.component.HAPPathToElement;

public class HAPDataAssociationActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String PATH = "path";

	private HAPPathToElement m_path;
	
	public HAPDataAssociationActivityExecutable(String id, HAPDataAssociationActivityDefinition activityDef) {
		super(activityDef.getActivityType(), id, activityDef);
		this.m_path = activityDef.getPathToElement();
	}

	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_path = new HAPPathToElement();
		this.m_path.buildObject(jsonObj.get(PATH), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
