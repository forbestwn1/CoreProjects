package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAny;

public class HAPOperandReference extends HAPOperandImp{

	public static final String EXPRESSIONNAME = "expressionName";
	
	private String m_expressionName;
	
	private HAPExpression m_expression;
	
	public HAPOperandReference(String expressionName){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE);
		this.m_expressionName = expressionName;
	}

	public String getExpressionName(){  return this.m_expressionName;  }
	
	public void setExpression(HAPExpression expression){ 
		this.m_expression = expression;  
		if(this.m_expression!=null){
			this.addChildOperand(this.m_expression.getOperand());
		}
	}
	public HAPExpression getExpression(){  return this.m_expression;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionName);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionName);
	}

	@Override
	public HAPConverters discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		//expression var info
		for(String varName : this.m_expression.getVariables().keySet()){
			HAPVariableInfo referenceVar = this.m_expression.getVariables().get(varName);
			HAPVariableInfo parentVar = variablesInfo.get(varName);
			if(parentVar==null){
				parentVar = new HAPVariableInfo(referenceVar.getCriteria());
			}
			else{
				HAPDataTypeCriteria criteria = dataTypeHelper.and(referenceVar, parentVar);
			}
			variablesInfo.put(varName, parentVar);
		}
		
		//clear variables info in expression 
		this.m_expression.getVariables().clear();
			
		return this.m_expression.getOperand().discover(variablesInfo, expectCriteria, context, dataTypeHelper);
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo, HAPDataTypeHelper dataTypeHelper) {
		HAPDataTypeCriteria out = this.m_expression.getOperand().normalize(variablesInfo, dataTypeHelper);
		this.setDataTypeCriteria(out);
		return out;
	}
}
