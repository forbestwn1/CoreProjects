package com.nosliw.core.application.entity.datarule.expression;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRule;
import com.nosliw.core.application.entity.datarule.HAPProviderDataRule;

@Component
public class HAPProviderDataRuleExpression implements HAPProviderDataRule{

	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPProviderDataRuleExpression(HAPParserDataExpression dataExpressionParser, HAPManagerWithVariablePlugin withVariableMan) {
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_withVariableMan = withVariableMan;
	}
	
	@Override
	public String getDataRuleType() {  return HAPConstantShared.DATARULE_TYPE_EXPRESSION;  }

	@Override
	public HAPPluginParserDataRule getParser() {	return new HAPPluginParserDataRuleExpression(this.m_dataExpressionParser);	}

	@Override
	public HAPPluginTransformerDataRule getTransformer() {  return new HAPPluginTransformerDataRuleExpression(this.m_withVariableMan);  }

}
