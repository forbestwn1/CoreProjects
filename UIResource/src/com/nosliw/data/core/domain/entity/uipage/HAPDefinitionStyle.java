package com.nosliw.data.core.domain.entity.uipage;

import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPDefinitionStyle {

	private String m_id;
	private String m_definition;

	public HAPDefinitionStyle(String id) {
		this.m_id = id;
	}
	
	public String getId() {   return this.m_id;    }
	
	public String getDefinition() {   return this.m_definition;   }
	public void setDefinition(String def) {    this.m_definition = def;     }

	public boolean isEmpty() {
		return HAPUtilityBasic.isStringEmpty(m_definition);
	}
}
