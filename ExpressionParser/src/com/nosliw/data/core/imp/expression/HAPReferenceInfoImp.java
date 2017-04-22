package com.nosliw.data.core.imp.expression;

import java.util.Map;

import com.nosliw.common.strvalue.HAPStringableValueEntity;

/**
 * The information used to define reference in expression
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public class HAPReferenceInfoImp extends HAPStringableValueEntity{

	public static String _VALUEINFO_NAME;
	
	private String m_reference;
	
	private Map<String, String> m_variableMap;
	
}
