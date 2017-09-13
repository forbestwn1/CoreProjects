package com.nosliw.data.core.criteria;

import com.nosliw.common.utils.HAPConstant;

/**
 * The criteria that is the result of a expression
 * It is used when define operation output criteria, when operation output criteria depends on operation input 
 *
 */
public class HAPDataTypeCriteriaExpression extends HAPDataTypeCriteriaAbstract{

	private String m_expression;
	
	private HAPDataTypeCriteria m_realCriteria;
	
	public HAPDataTypeCriteriaExpression(String expression){
		//escape    
		this.m_expression = expression.replaceAll(";;;", ":");
		this.m_expression = m_expression.replaceAll(";;", ",");
	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_EXPRESSION;	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_EXPRESSION));
		out.append(this.m_expression);
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_EXPRESSION));
		return out.toString(); 
	}

	public String getExpression(){  return this.m_expression;  }
	
}
