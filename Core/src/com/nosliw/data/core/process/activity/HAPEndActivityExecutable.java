package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPEndActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String RESULTNAME = "resultName";
	
	private String m_resultName;
	
	public HAPEndActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(HAPConstant.ACTIVITY_CATEGARY_END, id, activityDef);
		this.m_resultName = ((HAPEndActivityDefinition)activityDef).getName();
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_END))));
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
