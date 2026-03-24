package com.nosliw.core.application.entity.datarule.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRule;
import com.nosliw.core.application.entity.datarule.HAPProviderDataRule;

@Component
public class HAPProviderDataRuleExpression implements HAPProviderDataRule{

	@Autowired
	private HAPParserDataExpression m_dataExpressionParser;
	
	@Autowired
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	@Override
	public String getDataRuleType() {  return HAPConstantShared.DATARULE_TYPE_EXPRESSION;  }

	@Override
	public HAPPluginParserDataRule getParser() {	return new HAPPluginParserDataRuleExpression(this.m_dataExpressionParser);	}

	@Override
	public HAPPluginTransformerDataRule getTransformer() {  return new HAPPluginTransformerDataRuleExpression(this.m_withVariableMan);  }

}
