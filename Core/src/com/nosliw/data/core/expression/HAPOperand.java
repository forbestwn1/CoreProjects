package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeManager;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPOperand {

	@HAPAttribute
	public static final String TYPE = "type"; 

	@HAPAttribute
	public static final String STATUS = "status"; 
	
	@HAPAttribute
	public static final String CHILDREN = "children"; 

	@HAPAttribute
	public static final String DATATYPEINFO = "dataTypeInfo"; 

	@HAPAttribute
	public static final String CONVERTERS = "converters"; 
	
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
										HAPProcessVariablesContext context, 
										HAPDataTypeHelper dataTypeHelper);

	/**
	 * Normalize operand
	 * 		Output data : change out put data according to variablesInfo
	 * 		Create convertors 
	 * @param variablesInfo
	 * @return
	 */
	HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeHelper dataTypeHelper);
	
	//operand output data type criteria
	HAPDataTypeCriteria getDataTypeCriteria();
	
	//status of operand: new, processed, failed
	String getStatus();
	
	//get all the convertor required by this operand
	Set<HAPRelationship> getConverters();  
}
