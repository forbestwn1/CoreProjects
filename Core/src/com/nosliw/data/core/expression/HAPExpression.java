package com.nosliw.data.core.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Expression object we get after processing HAPExpressionDefinition
 *  
 */
@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExpression {

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String EXPRESSIONDEFINITION = "expressionDefinition";

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	@HAPAttribute
	public static String ERRORMSGS = "errorMsgs";

	@HAPAttribute
	public static String VARIABLESMATCHERS = "variablesMatchers";

	@HAPAttribute
	public static String REFERENCES = "references";
	
	String getId();
	void setId(String id);
	
	//after expression definition parsed, every expression has a unique id
	String getName();
	void setName(String name);
	
	//ExpressionDefinition used to define expression
	HAPExpressionDefinition getExpressionDefinition();
	
	//Operand to represent the expression
	HAPOperandWrapper getOperand();
	
	Set<String> getVariables();

	//Variables infos
	Map<String, HAPVariableInfo> getVariableInfos();

	//value for each variable, this converter help to convert to internal variable 
	Map<String, HAPMatchers> getVariableMatchers();
	
	//all the referenced expression, it may referenced by referenced in expression
	Map<String, HAPExpression> getReferences();
	
	//error message used to indicate whether the expression is successfully processed
	String[] getErrorMessages();

	//discover variable criteria / matchs in expression:
	//1 discover internal variable
	//2 discover expect variable
	//3 build converters between expect variable to internal variable
	HAPMatchers discover(Map<String, HAPVariableInfo> parentVariablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessExpressionDefinitionContext context,	HAPDataTypeHelper dataTypeHelper);
	
}
