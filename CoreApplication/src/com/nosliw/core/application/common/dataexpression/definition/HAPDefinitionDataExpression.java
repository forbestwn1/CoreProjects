package com.nosliw.core.application.common.dataexpression.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionDataExpression extends HAPSerializableImp{

	public static String OPERAND = "operand";
	
	private HAPDefinitionOperand m_operand;
	
	public HAPDefinitionDataExpression(HAPDefinitionOperand operand) {
		this.m_operand = operand;
	}
	
	public HAPDefinitionOperand getOperand() {    return this.m_operand;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OPERAND, this.m_operand.toStringValue(HAPSerializationFormat.JSON));
	}
}
