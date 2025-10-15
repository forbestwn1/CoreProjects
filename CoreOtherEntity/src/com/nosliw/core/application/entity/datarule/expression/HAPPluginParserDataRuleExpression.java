package com.nosliw.core.application.entity.datarule.expression;

import org.json.JSONObject;

import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;

public class HAPPluginParserDataRuleExpression implements HAPPluginParserDataRule{

	private HAPParserDataExpression m_dataExpressionParser;

	public HAPPluginParserDataRuleExpression(HAPParserDataExpression dataExpressionParser) {
		this.m_dataExpressionParser = dataExpressionParser;
	}
	
	@Override
	public HAPDataRule parse(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		HAPDataRuleExpression out = new HAPDataRuleExpression();
		out.setExpressionDefinition(this.m_dataExpressionParser.parseExpression(jsonObj.getString(HAPDataRuleExpression.EXPRESSION)));
		return out;
	}

}
