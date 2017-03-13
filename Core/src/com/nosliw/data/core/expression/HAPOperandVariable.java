package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPOperandVariable extends HAPOperandImp{

	public final static String VARIABLENAME = "variableName";
	
	protected String m_variableName;
	
	protected HAPDataTypeCriteria m_dataTypeCriteria;

	public HAPOperandVariable(String name, HAPDataTypeCriteriaManager criteriaMan){
		super(HAPConstant.EXPRESSION_OPERAND_VARIABLE, criteriaMan);
		this.m_variableName = name;
	}
	
	public String getVariableName(){  return this.m_variableName;  }
	public void setVariableName(String name){   this.m_variableName = name;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, m_variableName);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, m_variableName);
	}

	@Override
	public HAPDataTypeCriteria processVariable(Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria) {
		HAPDataTypeCriteria criteria = variablesInfo.get(this.m_variableName);
		variablesInfo.put(this.m_variableName, criteria);
		this.m_dataTypeCriteria = criteria;
		return criteria;
	}

	@Override
	public HAPDataTypeCriteria getDataTypeCriteria() {
		return this.m_dataTypeCriteria;
	}
}
