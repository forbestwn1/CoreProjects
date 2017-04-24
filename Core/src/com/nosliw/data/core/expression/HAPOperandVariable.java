package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperandVariable extends HAPOperandImp{

	public final static String VARIABLENAME = "variableName";
	
	protected String m_variableName;
	
//	protected HAPDataTypeCriteria m_dataTypeCriteria;

	public HAPOperandVariable(String name){
		super(HAPConstant.EXPRESSION_OPERAND_VARIABLE);
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
	public HAPDataTypeCriteria discover(
			Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		HAPDataTypeCriteria dataTypeCriteria = this.validate(variablesInfo.get(this.getVariableName()), expectCriteria, context, dataTypeHelper);
		variablesInfo.put(m_variableName, dataTypeCriteria);
		this.setDataTypeCriteria(dataTypeCriteria);
		return this.getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeHelper dataTypeHelper){
		this.setDataTypeCriteria(variablesInfo.get(this.getVariableName()));
		return this.getDataTypeCriteria();
	}
}
