package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPRelationship;
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
	 * Try best to process operand in order to discovery
	 * 		Variables:	narrowest variable criteria
	 * 		Output data type criteria: 
	 * @param variablesInfo  all the variables info in context
	 * @param expectCriteria expected output criteria for this operand
	 * @return  output criteria
	 */
	HAPDataTypeCriteria discover(Map<String, HAPDataTypeCriteria> variablesInfo,
										HAPDataTypeCriteria expectCriteria,
										HAPProcessVariablesContext context);

	/**
	 * Normalize operand
	 * 		Output data : change out put data according to variablesInfo
	 * 		Create convertors 
	 * @param variablesInfo
	 * @return
	 */
	HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo);
	
	//operand output data type criteria
	HAPDataTypeCriteria getDataTypeCriteria();
	
	//status of operand: new, processed, failed
	String getStatus();
	
	//get all the convertor required by this operand
	Set<HAPRelationship> getConverters();  
}
