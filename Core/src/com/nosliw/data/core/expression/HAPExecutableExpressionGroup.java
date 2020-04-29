package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.script.context.HAPContext;

//entity that can is runnable within runtime environment
@HAPEntityWithAttribute(baseName="EXPRESSIONGROUP")
public interface HAPExecutableExpressionGroup extends HAPSerializable, HAPExecutable{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	String getId();
	
	HAPContext getContext();
	void setContext(HAPContext context);
	
	Map<String, HAPVariableInfo> getVarsInfo();

	void addExpression(String name, HAPOperandWrapper operand);
	Map<String, HAPExecutableExpression> getExpressionItems();
	
	void updateVariableName(HAPUpdateName nameUpdate);
	
	void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker);
	
}