package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

/**
 * The criteria that is the result of a expression
 * It is used when define operation output criteria, when operation output criteria depends on operation input 
 *
 */
public class HAPDataTypeCriteriaExpression extends HAPDataTypeCriteriaImp{

	private String m_expression;
	
	public HAPDataTypeCriteriaExpression(String expression){
		this.m_expression = expression.replaceAll(";;", ",");
	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_EXPRESSION;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_EXPRESSION));
		out.append(this.m_expression);
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_EXPRESSION));
		return out.toString(); 
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

}
