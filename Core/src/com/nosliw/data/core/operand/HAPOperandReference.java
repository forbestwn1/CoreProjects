package com.nosliw.data.core.operand;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCENAME = "referenceName";
	
	private String m_referenceName;
	
	private HAPDefinitionDataAssociation m_inputMapping;
	
	private Map<String, String> m_variableMapping;

	private HAPExecutableExpression m_expression;
	
	private Map<String, HAPMatchers> m_matchers;
	
	private HAPOperandReference(){}
	
	public HAPOperandReference(String expressionName){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE);
		this.m_referenceName = expressionName;
	}

	public String getReferenceName(){  return this.m_referenceName;  }

	public HAPExecutableExpression getReferedExpression() {   return this.m_expression;   }
	public void setReferedExpression(HAPExecutableExpression expression) {   this.m_expression = expression;    }

	public void setInputMapping(HAPDefinitionDataAssociation inputMapping) {  this.m_inputMapping = inputMapping;   }
	public HAPDefinitionDataAssociation getInputMapping() {    return this.m_inputMapping;    }
	
	public Map<String, String> getVariableMapping(){   return this.m_variableMapping;   }
	public void setVariableMapping(Map<String, String> mapping) {   this.m_variableMapping = mapping;    }
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
//		Map<String, HAPMatchers> varConverters = this.m_referencedTask.getVariableMatchers();
//		for(String var : varConverters.keySet()){
//			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(varConverters.get(var).discoverRelationships()));
//		}
		return out;	
	}

	@Override
	public List<HAPResourceIdSimple> getResources() {
		List<HAPResourceIdSimple> out = super.getResources();
//		List<HAPResourceId> referenceResources = this.m_referencedTask.getResourceDependency(); 
//		out.addAll(referenceResources);
		return out;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCENAME, m_referenceName);
	}

	private String getInternalVariable(String ext) {
		return null;
	}
	
	private String getExternalVariable(String ext) {
		return null;
	}
	
	
	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		this.m_expression.discover(expectCriteria, processTracker);

		//variable
		Map<String, HAPVariableInfo> internalVariablesInfo = this.m_expression.getVarsInfo();
		for(String inVarName : internalVariablesInfo.keySet()) {
			String exVarName = getExternalVariable(inVarName);
			HAPVariableInfo inVar = internalVariablesInfo.get(inVarName);
			HAPVariableInfo exVar = variablesInfo.get(exVarName);
			HAPMatchers matchers = HAPCriteriaUtility.mergeVariableInfo(exVar, inVar.getCriteria(), dataTypeHelper);
			this.m_matchers.put(exVarName, matchers);
		}
		
		//output
		HAPDataTypeCriteria outputCriteria = this.m_expression.getOutputCriteria();
		return HAPCriteriaUtility.isMatchable(outputCriteria, expectCriteria, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandReference out = new HAPOperandReference();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandReference operand){
		super.cloneTo(operand);
		operand.m_referenceName = this.m_referenceName;
	}

}
