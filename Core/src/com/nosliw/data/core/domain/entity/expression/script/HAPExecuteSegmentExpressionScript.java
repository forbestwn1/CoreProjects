package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecuteSegmentExpressionScript implements HAPExecuteSegmentExpression{

	private String m_script;
	
	public HAPExecuteSegmentExpressionScript(String script) {
		this.m_script = script;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT;  }

	public String getScript() {    return this.m_script;     }
	
}
