package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression1.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression1.HAPScript;

public class HAPDefinitionUIEmbededScriptExpression extends HAPDefinitionScriptEntity{

	private String m_uiId;

	public HAPDefinitionUIEmbededScriptExpression(String uiId, String content) {
		super(HAPScript.newScript(content, HAPConstantShared.EXPRESSION_TYPE_LITERATE));
		this.setId(uiId);
		this.m_uiId = uiId;
	}
	
	public String getUIId() {   return this.m_uiId;  }
	
}
