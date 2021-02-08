package com.nosliw.data.core.data.criteria;

import com.nosliw.common.utils.HAPConstantShared;

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
		this.m_expression = HAPCriteriaUtility.deescape(expression);
	}
	
	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_EXPRESSION;	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_EXPRESSION));
		out.append(HAPCriteriaUtility.escape(this.m_expression));
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_EXPRESSION));
		return out.toString(); 
	}

	public String getExpression(){  return this.m_expression;  }
	
}
