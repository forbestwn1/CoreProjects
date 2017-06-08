package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperandVariable extends HAPOperandImp{

	public final static String VARIABLENAME = "variableName";
	
	protected String m_variableName;
	
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
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		HAPVariableInfo variableInfo = variablesInfo.get(this.getVariableName());
		if(variableInfo==null){
			//found a new variable
			variableInfo = new HAPVariableInfo();
			variablesInfo.put(this.getVariableName(), variableInfo);
		}
		
		if(variableInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
			//if variable info is open, calculate new criteria for this variable
			if(expectCriteria!=null){
				if(variableInfo.getCriteria()==null){
					variableInfo.setCriteria(expectCriteria);
				}
				else{
					HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(variableInfo.getCriteria(), expectCriteria);
					if(adjustedCriteria==null){
						context.addMessage("error");
						return null;
					}
					else{
						variableInfo.setCriteria(adjustedCriteria);
					}
				}
			}
		}
		
		//set output criteria
		this.setOutputCriteria(variableInfo.getCriteria());
		//cal converter
		return this.isMatchable(variableInfo.getCriteria(), expectCriteria, context, dataTypeHelper);
	}
}
