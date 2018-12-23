package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;

//entity that can is runnable within runtime environment
@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExecutableExpression extends HAPSerializable{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLESMATCHERS = "variablesMatchers";

	String getId();
	
	//Operand to represent the expression
	HAPOperandWrapper getOperand();

	Map<String, HAPMatchers> getVariableMatchers();

	public static void buildJsonMap(HAPExecutableExpression obj, Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ID, obj.getId());
		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(obj.getOperand(), HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLESMATCHERS, HAPJsonUtility.buildJson(obj.getVariableMatchers(), HAPSerializationFormat.JSON));
	}
	
}
