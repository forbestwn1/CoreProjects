package com.nosliw.data.core.activity.plugin.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.process1.HAPDefinitionSequenceFlow;
import com.nosliw.data.core.process1.HAPExecutableActivity;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPStartActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String FLOW = "flow";

	private HAPDefinitionSequenceFlow m_flow;
	
	public HAPStartActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(HAPConstantShared.ACTIVITY_CATEGARY_START, id, activityDef);
	}

	public void setFlow(HAPDefinitionSequenceFlow flow) { this.m_flow = flow;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_flow = new HAPDefinitionSequenceFlow();
		this.m_flow.buildObject(jsonObj.getJSONObject(FLOW), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_flow!=null)		jsonMap.put(FLOW, this.m_flow.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_START))));
		return out;
	}
	
}
