package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperandReference extends HAPOperandImp{

	public static final String EXPRESSIONNAME = "expressionName";
	
	private String m_expressionReference;
	
	private HAPExpression m_referencedExpression;
	
	public HAPOperandReference(String expressionName){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE);
		this.m_expressionReference = expressionName;
	}

	public String getExpressionReference(){  return this.m_expressionReference;  }
	
	public void setExpression(HAPExpression expression){ 		this.m_referencedExpression = expression;	}
	public HAPExpression getExpression(){  return this.m_referencedExpression;  }
	
	@Override
	public Set<HAPRelationship> getConverters(){
		Set<HAPRelationship> out = new HashSet<HAPRelationship>();
		Map<String, HAPMatchers> varConverters = this.m_referencedExpression.getVariableMatchers();
		for(String var : varConverters.keySet()){
			out.addAll(varConverters.get(var).getRelationships());
		}
		return out;	
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionReference);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONNAME, m_expressionReference);
	}

	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		
		return this.m_referencedExpression.discover(variablesInfo, expectCriteria, context, dataTypeHelper);
	}
}
