package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionSegmentExpressionScript implements HAPDefinitionSegmentExpression{

	private String m_script;
	
	public HAPDefinitionSegmentExpressionScript(String script) {
		this.m_script = script;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT;  }

	public String getScript() {    return this.m_script;     }
	
}
