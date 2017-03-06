package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeCriteria;

public class HAPOperandVariable extends HAPOperandImp{

	public final static String VARIABLENAME = "variableName";
	
	protected String m_variableName;
	
	protected HAPDataTypeCriteria m_dataTypeCriteria;

	public HAPOperandVariable(String name){
		this.m_variableName = name;
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_VARIABLE;}
	
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
}
