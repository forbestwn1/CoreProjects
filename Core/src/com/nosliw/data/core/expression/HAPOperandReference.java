package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperandReference extends HAPOperandImp{

	public static final String EXPRESSIONNAME = "expressionName";
	
	private String m_expressionName;
	
	private HAPExpression m_expression;
	
	public HAPOperandReference(String expressionName){
		this.m_expressionName = expressionName;
	}

	@Override
	public String getType() {		return HAPConstant.EXPRESSION_OPERAND_REFERENCE;	}

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
	public HAPDataTypeCriteria process(HAPExpressionInfo expressionInfo) {
		if(this.m_expression==null){
			
		}
		
		//merge variable info
		
		return this.m_expression.getOperand().getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria getDataTypeCriteria() {
		return this.m_expression.getOperand().getDataTypeCriteria();
	}
}
