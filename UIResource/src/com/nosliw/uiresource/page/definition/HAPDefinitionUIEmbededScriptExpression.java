package com.nosliw.uiresource.page.definition;

import com.nosliw.data.core.script.expression.HAPDefinitionEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpression;

public class HAPDefinitionUIEmbededScriptExpression extends HAPDefinitionEmbededScriptExpression{

	private String m_uiId;

	public HAPDefinitionUIEmbededScriptExpression(String uiId, HAPDefinitionScriptExpression scriptExpression) {
		super(scriptExpression);
		this.m_uiId = uiId;
	}

	public HAPDefinitionUIEmbededScriptExpression(String uiId, String content) {
		super(content);
		this.m_uiId = uiId;
	}
	
	public String getUIId() {   return this.m_uiId;  }
	
}
