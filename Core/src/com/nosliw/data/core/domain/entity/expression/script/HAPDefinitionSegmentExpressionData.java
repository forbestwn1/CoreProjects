package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionSegmentExpressionData implements HAPDefinitionSegmentExpression{

	private String m_dataExpressionId;
	
	public HAPDefinitionSegmentExpressionData(String dataExpressionId) {
		this.m_dataExpressionId = dataExpressionId;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION;  }

	public String getDataExpressionId() {    return this.m_dataExpressionId;     }
	
}
