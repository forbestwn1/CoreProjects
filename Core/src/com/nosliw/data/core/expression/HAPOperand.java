package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPOperand {

	public static final String TYPE = "type"; 

	public static final String CHILDREN = "children"; 

	public static final String DATATYPEINFO = "dataTypeInfo"; 

	//type of operand
	String getType();

	//children operand
	List<HAPOperand> getChildren();

	/**
	 * Process variables in operand 
	 * @param variablesInfo  all the variables info in context
	 * @param expectCriteria expected output criteria for this operand
	 * @return  output criteria
	 */
	HAPDataTypeCriteria discoverVariables(Map<String, HAPDataTypeCriteria> variablesInfo,
										HAPDataTypeCriteria expectCriteria,
										HAPProcessVariablesContext context);

	HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo);
	
	//operand output data type criteria
	HAPDataTypeCriteria getDataTypeCriteria();
	
	//status of operand: new, processed, failed
	String getStatus();
	
}
