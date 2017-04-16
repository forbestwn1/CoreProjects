package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPOperandReference extends HAPOperandImp{

	public static final String EXPRESSIONNAME = "expressionName";
	
	private String m_expressionName;
	
	private HAPExpression m_expression;
	
	public HAPOperandReference(String expressionName, HAPDataTypeCriteriaManager criteriaMan){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE, criteriaMan);
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
	public HAPDataTypeCriteria discover(
			Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria,
			HAPProcessVariablesContext context) {
		//expression var info
		for(String varName : this.m_expression.getVariables().keySet()){
			HAPDataTypeCriteria criteria = this.getDataTypeCriteriaManager().and(this.m_expression.getVariables().get(varName), variablesInfo.get(varName));
			variablesInfo.put(varName, criteria);
		}
		
		//clear variables info in expression 
		this.m_expression.getVariables().clear();
			
		HAPDataTypeCriteria out = this.m_expression.getOperand().discover(variablesInfo, expectCriteria, context);
		this.setDataTypeCriteria(out);
		return out;
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo) {
		HAPDataTypeCriteria out = this.m_expression.getOperand().normalize(variablesInfo);
		this.setDataTypeCriteria(out);
		return out;
	}
}
