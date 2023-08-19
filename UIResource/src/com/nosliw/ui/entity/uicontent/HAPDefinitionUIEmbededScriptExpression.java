package com.nosliw.ui.entity.uicontent;

public class HAPDefinitionUIEmbededScriptExpression{

	private String m_uiId;
	
	private String m_scriptId;

	public HAPDefinitionUIEmbededScriptExpression(String uiId, String scriptId) {
		this.m_uiId = uiId;
		this.m_scriptId = scriptId;
	}
	
	public String getUIId() {   return this.m_uiId;  }
	
	public String getScriptId() {    return this.m_scriptId;     }
	
}
