package com.nosliw.data.core.process.resource;

import org.json.JSONObject;

import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceDefinitionProcess extends HAPParserResourceDefinitionImp{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParserResourceDefinitionProcess(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		return HAPParserProcessDefinition.parsePocessSuite(jsonObj, m_activityPluginMan);
	}

}
