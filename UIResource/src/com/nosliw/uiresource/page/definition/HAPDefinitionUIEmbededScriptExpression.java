package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPScript;

public class HAPDefinitionUIEmbededScriptExpression extends HAPScript{

	private String m_uiId;

	public HAPDefinitionUIEmbededScriptExpression(String uiId, String content) {
		super(content, HAPConstant.SCRIPT_TYPE_LITERATE);
		this.m_uiId = uiId;
	}
	
	public String getUIId() {   return this.m_uiId;  }
	
}
