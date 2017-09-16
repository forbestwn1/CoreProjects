package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * ExpressionDefinition is the basic unit to define a expression 
 * All information required when describe a expression
 */
@HAPEntityWithAttribute(baseName="EXPRESSIONDEFINITION")
public interface HAPExpressionDefinition {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String OPERAND = "operand";

	@HAPAttribute
	public static String CONSTANTS = "constants";
	
	@HAPAttribute
	public static String VARIABLECRITERIAS = "variableCriterias";
	
	//all the information for references in expression definition, for instance, variable mapping
	@HAPAttribute
	public static String REFERENCES = "references";

	//expression name, should be unique
	String getName();
	void setName(String name);
	
	//related information, for instance, description, 
	HAPInfo getInfo();
	
	//expression as string
	String getExpression();
	
	//the operand after parsing the expression
	HAPOperand getOperand();
	void setOperand(HAPOperand operand);
	
	//constants definition
	Map<String, HAPData> getConstants();
	
	//variables definition
	Map<String, HAPDataTypeCriteria> getVariableCriterias();
	void setVariableCriterias(Map<String, HAPDataTypeCriteria> varCriterias);
	
	//references definition
	Map<String, HAPReferenceInfo> getReferences();
	
	HAPExpressionDefinition cloneExpressionDefinition();
}
