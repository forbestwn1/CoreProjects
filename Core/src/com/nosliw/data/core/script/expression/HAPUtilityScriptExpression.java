package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPResourceDefinitionExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;

public class HAPUtilityScriptExpression {


	public static Map<String, HAPData> getConstantData(Map<String, Object> constantsValue){
		Map<String, HAPData> constantsData = new LinkedHashMap<String, HAPData>();
		for(String name : constantsValue.keySet()) {
			if(constantsValue.get(name) instanceof HAPData) {
				constantsData.put(name, (HAPData)constantsValue.get(name));
			}
		}
		return constantsData;
	}
	
	//process variables in expression, 
	//	for attribute operation a.b.c.d which have responding definition in context, 
	//			replace attribute operation with one variable operation
	//  for attribute operation a.b.c.d which have responding definition a.b.c in context, 
	//			replace attribute operation with one variable operation(a.b.c) and getChild operation
	public static void processAttributeOperandInExpression(HAPResourceDefinitionExpression expressionDefinition, final Map<String, HAPVariableInfo> varsInfo){
		HAPOperandUtility.processAttributeOperandInExpressionOperand(expressionDefinition.getOperand(), varsInfo);
	}
	

}

