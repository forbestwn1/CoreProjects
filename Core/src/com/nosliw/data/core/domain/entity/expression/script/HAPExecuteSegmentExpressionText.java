package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecuteSegmentExpressionText implements HAPExecuteSegmentExpression{

	private String m_content;
	
	public HAPExecuteSegmentExpressionText(String content) {
		this.m_content = content;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT;  }

	public String getContent() {    return this.m_content;     }
	
}
