package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Expression object we get after processing HAPExpressionInfo
 *  
 */
@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExpression {

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String EXPRESSIONINFO = "expressionInfo";

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLES = "variables";
	
	@HAPAttribute
	public static String ERRORMSGS = "errorMsgs";

	String getId();
	void setId(String id);
	
	//ExpressionInfo used to define expression
	HAPExpressionInfo getExpressionInfo();
	
	//Operand to represent the expression
	HAPOperand getOperand();
	
	//Variables infos
	Map<String, HAPVariableInfo> getVariables();

	//value for each variable, this converter help to convert to internal variable 
	Map<String, HAPConverters> getVariableConverters();
	
	//error message used to indicate whether the expression is successfully processed
	String[] getErrorMessages();

	//discover variable infor in expression:
	//1 discover internal variable
	//2 discover expect variable
	//3 build converters between expect variable to internal variable
	HAPConverters discover(Map<String, HAPVariableInfo> expectVariablesInfo, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context,	HAPDataTypeHelper dataTypeHelper);
	
}
