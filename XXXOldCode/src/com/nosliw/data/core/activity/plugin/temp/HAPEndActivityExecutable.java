package com.nosliw.data.core.activity.plugin.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPPluginResourceIdActivity;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.process1.HAPExecutableActivity;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPEndActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String RESULTNAME = "resultName";
	
	private String m_resultName;
	
	public HAPEndActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(HAPConstantShared.ACTIVITY_CATEGARY_END, id, activityDef);
		this.m_resultName = ((HAPEndActivityDefinition)activityDef).getName();
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPPluginResourceIdActivity(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_END))));
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_resultName = jsonObj.getString(RESULTNAME);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESULTNAME, this.m_resultName);
	}
}
