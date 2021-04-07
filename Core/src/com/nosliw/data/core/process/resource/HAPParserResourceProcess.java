package com.nosliw.data.core.process.resource;

import org.json.JSONObject;

import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPParserResourceImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceProcess extends HAPParserResourceImp{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParserResourceProcess(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		return HAPParserProcessDefinition.parsePocessSuite(jsonObj, m_activityPluginMan);
	}

}
