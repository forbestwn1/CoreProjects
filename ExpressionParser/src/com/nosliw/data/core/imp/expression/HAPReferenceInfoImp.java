package com.nosliw.data.core.imp.expression;

import java.util.Map;

import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.core.task111.expression.HAPReferenceInfo;

/**
 * The information used to define reference in expression
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public class HAPReferenceInfoImp{ 
/*
extends HAPStringableValueEntity implements HAPReferenceInfo{

	public static String _VALUEINFO_NAME;
	
	@Override
	public String getReference() {		return this.getAtomicAncestorValueString(REFERENCE);	}

	@Override
	public Map<String, String> getVariablesMap() {		
		return this.getMapValueAncestorByPath(VARIABLESMAP);	
	}

	public void setVariableMap(Map<String, String> variablesMap){
		HAPStringableValueMap mapValue = this.getMapAncestorByPath(VARIABLESMAP);
		mapValue.clear();
		for(String fromVar : variablesMap.keySet()){
			mapValue.updateChild(fromVar, new HAPStringableValueAtomic(variablesMap.get(fromVar)));
		}
	}
	*/
}
