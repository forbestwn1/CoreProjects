package com.nosliw.data.core.story.resource;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.script.context.HAPParmDefinition;

public class HAPDefinitionTemplate extends HAPEntityInfoImp{

	//build output type
	private String m_resourceType;
	
	//parameter definition
	private Map<String, HAPParmDefinition> m_parmsDefinition;
	
	//builder to build resource from template and input parameter
	private String m_builderId;
	
	public String getResourceType() {   return this.m_resourceType;  }

	public String getBuilderId() {   return this.m_builderId;    }
	
}
