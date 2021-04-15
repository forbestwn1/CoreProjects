package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.script.context.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextStructure;

//entity that can is runnable within runtime environment
@HAPEntityWithAttribute(baseName="EXPRESSIONGROUP")
public interface HAPExecutableExpressionGroup extends HAPSerializable, HAPExecutable{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	String getId();
	
	HAPContextStructure getContextStructure();
	void setContextStructure(HAPContextStructure contextStructure);

	HAPContextFlat getContextFlat();
	
	HAPContainerVariableCriteriaInfo getVarsInfo();

	void addExpression(String name, HAPOperandWrapper operand);
	Map<String, HAPExecutableExpression> getExpressionItems();
	
	void updateVariableName(HAPUpdateName nameUpdate);
	
	void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker);
	
}
