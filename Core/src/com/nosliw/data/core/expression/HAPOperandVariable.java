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
	public HAPDataTypeCriteria discoverVariables(
			Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context) {
		this.m_dataTypeCriteria = this.validate(variablesInfo.get(this.m_variableName), expectCriteria, context);
		variablesInfo.put(m_variableName, m_dataTypeCriteria);
		this.setDataTypeCriteria(m_dataTypeCriteria);
		return this.getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo){
		this.setDataTypeCriteria(variablesInfo.get(this.getVariableName()));
		return this.getDataTypeCriteria();
	}
}
