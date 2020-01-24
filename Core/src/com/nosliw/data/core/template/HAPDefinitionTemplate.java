package com.nosliw.data.core.template;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.script.context.HAPContextEntity;

public class HAPDefinitionTemplate extends HAPEntityInfoImp{

	//build output type
	private String m_resourceType;
	
	//parameter definition
	private HAPContextEntity m_parmsDefinition;
	
	//template content
	private HAPContentTemplate m_content;
	
	//builder to build resource from template and input parameter
	private String m_builderId;
	
}
