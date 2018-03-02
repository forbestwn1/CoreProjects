package com.nosliw.data.core.operand;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.task.HAPProcessTaskContext;

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
			HAPProcessTaskContext context,
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
