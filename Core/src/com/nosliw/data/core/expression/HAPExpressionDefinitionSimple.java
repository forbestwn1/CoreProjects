package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPExpressionDefinitionSimple implements HAPExpressionDefinition{

	private String m_name; 
	
	private String m_expression;
	
	private Map<String, HAPData> m_constants;
	
	private Map<String, HAPDataTypeCriteria> m_variableCriterias; 
	
	private Map<String, HAPReferenceInfo> m_references;
	
	private HAPInfo m_info;
	
	public HAPExpressionDefinitionSimple(
			String expression, 
			String name, 
			Map<String, HAPData> constants,
			Map<String, HAPDataTypeCriteria> variableCriterias, 
			Map<String, HAPReferenceInfo> references, 
			HAPInfo info){
		this.m_expression = expression;
		this.m_name = name;
		this.m_constants = constants;
		this.m_variableCriterias = variableCriterias;
		this.m_references = references;
		this.m_info = info;
	}
	
	@Override
	public String getName() {		return this.m_name;	}

	@Override
	public HAPInfo getInfo() {		return this.m_info;	}

	@Override
	public String getExpression() {		return this.m_expression;	}

	@Override
	public Map<String, HAPData> getConstants() {		return this.m_constants;	}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariableCriterias() {		return this.m_variableCriterias;	}

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {		return this.m_references;	}

}
