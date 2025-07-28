package com.nosliw.data.core.process1.resource;

import org.json.JSONObject;

import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.process1.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPParserResourceEntityImp;
import com.nosliw.data.core.resource.HAPResourceDefinition1;

public class HAPParserResourceDefinitionProcess extends HAPParserResourceEntityImp{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParserResourceDefinitionProcess(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public HAPResourceDefinition1 parseJson(JSONObject jsonObj) {
		return HAPParserProcessDefinition.parsePocessSuite(jsonObj, m_activityPluginMan);
	}

}
