package com.nosliw.core.application.entity.datarule.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

@HAPEntityWithAttribute
public class HAPDataRuleExpression extends HAPDataRule{

	@HAPAttribute
	public static String EXPRESSION = "expression";

//	@HAPAttribute
//	public static String VARIABLENAME = "data";

	
	private HAPDefinitionDataExpression m_expressionDef;

	public HAPDataRuleExpression() {
		super(HAPConstantShared.DATARULE_TYPE_EXPRESSION);
	}

	public HAPDataRuleExpression(HAPDefinitionDataExpression expression) {
		this();
		this.m_expressionDef = expression;
	}
	
	public HAPDefinitionDataExpression getExpressionDefinition() {   return this.m_expressionDef;   }
	public void setExpressionDefinition(HAPDefinitionDataExpression expression) {   this.m_expressionDef = expression;    }

//	public HAPExecutableEntityExpressionDataGroup getExpressionExecutable() {    return this.m_expressionExe;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPUtilityJson.buildJson(this.getExpressionDefinition(), HAPSerializationFormat.JSON));
	}
	
}
