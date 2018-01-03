package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceUtility;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCENAME = "referenceName";
	
	private String m_expressionReference;
	
	private HAPExpression m_referencedExpression;

	private HAPOperandReference(){}
	
	public HAPOperandReference(String expressionName){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE);
		this.m_expressionReference = expressionName;
	}

	public String getExpressionReference(){  return this.m_expressionReference;  }
	
	public void setExpression(HAPExpression expression){ 		this.m_referencedExpression = expression;	}
	public HAPExpression getExpression(){  return this.m_referencedExpression;  }
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		Map<String, HAPMatchers> varConverters = this.m_referencedExpression.getVariableMatchers();
		for(String var : varConverters.keySet()){
			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(varConverters.get(var).discoverRelationships()));
		}
		return out;	
	}

	@Override
	public List<HAPResourceId> getResources() {
		List<HAPResourceId> out = super.getResources();
		List<HAPResourceId> referenceResources = HAPExpressionUtility.discoverResources(this.getExpression());
		out.addAll(referenceResources);
		return out;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCENAME, m_expressionReference);
	}

	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessExpressionDefinitionContext context,
			HAPDataTypeHelper dataTypeHelper) {
		
		return this.m_referencedExpression.discover(variablesInfo, expectCriteria, context, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandReference out = new HAPOperandReference();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandReference operand){
		super.cloneTo(operand);
		operand.m_expressionReference = this.m_expressionReference;
	}

}
