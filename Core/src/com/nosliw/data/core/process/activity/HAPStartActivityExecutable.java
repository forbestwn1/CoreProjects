package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionSequenceFlow;
import com.nosliw.data.core.process.HAPExecutableActivity;

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
	
}
