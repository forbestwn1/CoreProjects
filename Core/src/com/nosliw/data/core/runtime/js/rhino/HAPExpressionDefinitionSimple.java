package com.nosliw.data.core.runtime.js.rhino;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionDefinitionSimple implements HAPExpressionDefinition{

	private String m_name; 
	
	public HAPExpressionDefinitionSimple(
			String expression, 
			String name, 
			Map<String, HAPData> constants,
			Map<String, HAPDataTypeCriteria> variableCriterias, 
			Map<String, HAPReferenceInfo> references, 
			HAPInfo info){
		
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public HAPInfo getInfo() {
		return null;
	}

	@Override
	public String getExpression() {
		return null;
	}

	@Override
	public Map<String, HAPData> getConstants() {
		return null;
	}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariableCriterias() {
		return null;
	}

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {
		return null;
	}

}
