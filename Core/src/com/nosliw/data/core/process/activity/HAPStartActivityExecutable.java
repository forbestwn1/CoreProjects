package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionSequenceFlow;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.runtime.HAPResourceDependent;

public class HAPStartActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String FLOW = "flow";

	private HAPDefinitionSequenceFlow m_flow;
	
	public HAPStartActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}

	public void setFlow(HAPDefinitionSequenceFlow flow) { this.m_flow = flow;  }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_flow!=null)		jsonMap.put(FLOW, this.m_flow.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency() {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(HAPConstant.ACTIVITY_TYPE_START)));
		return out;
	}
	
}
