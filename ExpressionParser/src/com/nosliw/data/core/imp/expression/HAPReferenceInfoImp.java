package com.nosliw.data.core.imp.expression;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.expression.HAPReferenceInfo;

/**
 * The information used to define reference in expression
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public class HAPReferenceInfoImp extends HAPStringableValueEntity implements HAPReferenceInfo{

	public static String _VALUEINFO_NAME;
	
	private String m_reference;
	
	private Map<String, String> m_variableMap;

	@Override
	public String getReference() {		return this.getAtomicAncestorValueString(REFERENCE);	}

	@Override
	public Map<String, String> getVariableMap() {		return this.getMapValueAncestorByPath(VARIABLEMAP);	}
}
