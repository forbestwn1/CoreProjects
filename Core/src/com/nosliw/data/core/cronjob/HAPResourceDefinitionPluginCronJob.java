package com.nosliw.data.core.cronjob;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceDefinitionPluginCronJob implements HAPPluginResourceDefinition{

	private HAPParserCronJobDefinition m_cronJobParser;
	
	public HAPResourceDefinitionPluginCronJob(HAPParserCronJobDefinition cronJobParser) {
		this.m_cronJobParser = cronJobParser;
	}
	
	@Override
	public String getResourceType() {	return HAPConstant.RUNTIME_RESOURCE_TYPE_CRONJOB;	}

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		String file = HAPFileUtility.getUIModuleFolder()+resourceId.getId()+".res";
		HAPDefinitionCronJob cronJobDef = m_cronJobParser.parseFile(file);
		return cronJobDef;
	}

}
