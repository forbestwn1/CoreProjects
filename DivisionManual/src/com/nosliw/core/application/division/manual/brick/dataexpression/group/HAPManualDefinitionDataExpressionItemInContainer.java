package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;

public class HAPManualDefinitionDataExpressionItemInContainer extends HAPEntityInfoImp{

	private HAPDefinitionDataExpression m_expression;
	
	public HAPManualDefinitionDataExpressionItemInContainer(HAPDefinitionDataExpression expression) {
		this.m_expression = expression;
	}
	
	public HAPDefinitionDataExpression getExpression() {    return this.m_expression;     }
	public void setExpression(HAPDefinitionDataExpression expression) {    this.m_expression = expression;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPItemInContainerDataExpression.DATAEXPRESSION, m_expression.toStringValue(HAPSerializationFormat.JSON));
	}

}
