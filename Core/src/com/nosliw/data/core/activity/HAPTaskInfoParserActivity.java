package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPParserTask;

public class HAPTaskInfoParserActivity implements HAPParserTask{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPTaskInfoParserActivity(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}

	@Override
	public HAPDefinitionTask parseTaskDefinition(Object obj, HAPDefinitionEntityComplex complexEntity) {
		return HAPParserActivity.parseActivityDefinition((JSONObject)obj, complexEntity, m_activityPluginMan);	
	}

}
