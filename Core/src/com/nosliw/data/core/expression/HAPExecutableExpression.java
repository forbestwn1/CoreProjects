package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;

//entity that can is runnable within runtime environment
@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExecutableExpression extends HAPSerializable, HAPExecutable{

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	String getId();
	
	//Operand to represent the expression
	HAPOperandWrapper getOperand();

	Map<String, HAPVariableInfo> getVarsInfo();

	HAPMatchers getOutputMatchers();
	
	void updateVariableName(HAPUpdateName nameUpdate);
	
	HAPDefinitionExpression getDefinition();

	void discover(HAPDataTypeCriteria expectOutput, HAPProcessTracker processTracker);
	
	HAPDataTypeCriteria getOutputCriteria();

	public static void buildJsonMap(HAPExecutableExpression obj, Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(obj.getOperand(), HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(obj.getOutputMatchers(), HAPSerializationFormat.JSON));
	}
	
}
