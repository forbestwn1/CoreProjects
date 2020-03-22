package com.nosliw.data.core.operand;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandVariable extends HAPOperandImp{
 
	@HAPAttribute
	public final static String VARIABLENAME = "variableName";
	
	protected String m_variableName;
	
	private HAPOperandVariable(){}
	
	public HAPOperandVariable(String name){
		super(HAPConstant.EXPRESSION_OPERAND_VARIABLE);
		this.m_variableName = name;
	}
	
	public String getVariableName(){  return this.m_variableName;  }
	public void setVariableName(String name){   this.m_variableName = name;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, m_variableName);
	}

	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		
		HAPVariableInfo variableInfo = variablesInfo.get(this.getVariableName());
		if(variableInfo==null){
			//found a new variable
			variableInfo = HAPVariableInfo.buildUndefinedVariableInfo();
			variablesInfo.put(this.getVariableName(), variableInfo);
		}
		
		HAPMatchers matchers = HAPCriteriaUtility.mergeVariableInfo(variableInfo, expectCriteria, dataTypeHelper);
		
		//set output criteria
		this.setOutputCriteria(variableInfo.getCriteria());

		//cal converter
		return matchers;
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandVariable out = new HAPOperandVariable();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandVariable operand){
		super.cloneTo(operand);
		operand.m_variableName = this.m_variableName;
	}
	
}
