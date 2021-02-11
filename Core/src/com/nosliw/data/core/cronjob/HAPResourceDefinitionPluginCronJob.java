package com.nosliw.data.core.cronjob;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginCronJob implements HAPPluginResourceDefinition{

	private HAPParserCronJobDefinition m_cronJobParser = HAPParserCronJobDefinition.getInstance();
	
	public HAPResourceDefinitionPluginCronJob() {
	}
	
	@Override
	public String getResourceType() {	return HAPConstantShared.RUNTIME_RESOURCE_TYPE_CRONJOB;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String file = HAPSystemFolderUtility.getCronJobFolder()+resourceId.getId()+".res";
		HAPDefinitionCronJob cronJobDef = m_cronJobParser.parseFile(file);
		return cronJobDef;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return this.m_cronJobParser.parseCronJob(jsonObj, null);
	}

}
