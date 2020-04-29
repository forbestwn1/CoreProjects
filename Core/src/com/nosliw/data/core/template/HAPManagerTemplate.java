package com.nosliw.data.core.template;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;

public class HAPManagerTemplate {

	private HAPManagerResourceDefinition m_resourceDefManager;
	
	public HAPManagerTemplate(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
	}

	public HAPResourceDefinitionTemplate getTemplate(String id) {
		return (HAPResourceDefinitionTemplate)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdTemplate(new HAPTemplateId(id)));
	}
	
}
