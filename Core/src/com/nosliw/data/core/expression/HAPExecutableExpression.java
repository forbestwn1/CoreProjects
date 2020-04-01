package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.script.context.HAPContext;

//entity that can is runnable within runtime environment
@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExecutableExpression extends HAPSerializable, HAPExecutable{

	String getId();
	
	HAPContext getContext();
	void setContext(HAPContext context);
	
	Map<String, HAPVariableInfo> getVarsInfo();

	void addExpression(String name, HAPOperandWrapper operand);
	Map<String, HAPExecutableExpressionItem> getExpressions();
	
	void updateVariableName(HAPUpdateName nameUpdate);
	
	HAPDefinitionExpression getDefinition();

	void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPProcessTracker processTracker);
	
//	public static void buildJsonMap(HAPExecutableExpression obj, Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
//		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(obj.getOperand(), HAPSerializationFormat.JSON));
//		jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(obj.getOutputMatchers(), HAPSerializationFormat.JSON));
//	}
}
