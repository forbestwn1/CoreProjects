package com.nosliw.data.core.expression;

import java.util.List;

import com.nosliw.data.core.HAPDataTypeCriteria;

public interface HAPOperand {

	public static final String TYPE = "type"; 

	public static final String CHILDREN = "children"; 

	public static final String DATATYPEINFO = "dataTypeInfo"; 
	
	String getType();

	List<HAPOperand> getChildren();

	HAPDataTypeCriteria getDataTypeInfo();

}
