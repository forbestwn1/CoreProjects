package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPOperand {

	public static final String TYPE = "type"; 

	public static final String CHILDREN = "children"; 

	public static final String DATATYPEINFO = "dataTypeInfo"; 
	
	String getType();

	List<HAPOperand> getChildren();

	HAPDataTypeCriteria processVariable(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeCriteria expectCriteria);

	HAPDataTypeCriteria getDataTypeCriteria();
	
	String getStatus();
	
}
