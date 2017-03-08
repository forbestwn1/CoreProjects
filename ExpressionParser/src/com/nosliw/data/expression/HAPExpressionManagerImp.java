package com.nosliw.data.expression;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private static HAPExpressionManagerImp m_instance;

	private Map<String, HAPExpressionInfo> m_expressionInfos;
	
	public static HAPExpressionManagerImp getInstance(){
		if(m_instance==null){
			m_instance = new HAPExpressionManagerImp();
		}
		return m_instance;
	}
	
	
	public HAPExpressionManagerImp(){
		HAPValueInfoManager.getInstance().importFromXML(HAPExpressionManagerImp.class, new String[]{
				"expression.xml",
				"referenceinfo.xml"
		});
		
		this.m_expressionInfos = new LinkedHashMap<String, HAPExpressionInfo>();
		
	}


	@Override
	public HAPExpressionInfo getExpressionInfo(String name) {
		return this.m_expressionInfos.get(name);
	}

	private void mergeVariableInfo(Map<String, HAPDataTypeCriteria> baseVariablesInfo, String varName, HAPDataTypeCriteria variableInfo){
		HAPDataTypeCriteria baseVarInfo = baseVariablesInfo.get(varName);
		if(baseVarInfo==null){
			baseVariablesInfo.put(varName, variableInfo);
		}
		else{
			baseVarInfo.merge(variableInfo);
		}
	}
	
	public void processVariables(HAPExpressionImp expression, final Map<String, HAPDataTypeCriteria> variablesInfo) {
		
		//get variables information
		this.processAllOperand(expression.getOperand(), null, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceChild = (HAPOperandReference)operand;
					HAPExpressionImp referenceExpression = (HAPExpressionImp)referenceChild.getExpression();
					Map<String, HAPDataTypeCriteria> referenceVarsInfo = new LinkedHashMap<String, HAPDataTypeCriteria>();
					referenceVarsInfo.putAll(referenceExpression.getExpressionInfo().getVariables());
					processVariables(referenceExpression, referenceVarsInfo);
					
					for(String v : referenceVarsInfo.keySet()){
						mergeVariableInfo(variablesInfo, v, referenceVarsInfo.get(v));
					}
					return false;
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand;
					mergeVariableInfo(variablesInfo, variableChild.getVariableName(), null);
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_OPERATION)){
					
				}
				return true;
			}});

	}

	public HAPExpression processReferences(String expressionName, Map<String, String> varsMapping) {
		//replace all reference
		final HAPExpressionInfo expressionInfo = this.getExpressionInfo(expressionName);
		
		HAPOperand expressionOperand = HAPExpressionParser.parseExpression(expressionInfo.getExpression());
		
		this.processAllOperand(expressionOperand, varsMapping, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> varMap = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceChild = (HAPOperandReference)operand;
					
					String referenceName = referenceChild.getExpressionName();
					HAPReferenceInfo referenceInfo = expressionInfo.getReferences().get(referenceName);
					HAPExpression referenceExpression = processReferences(referenceName, referenceInfo.getVariableMap());
					referenceChild.setExpression(referenceExpression);
					return false;
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand;
					String mappedVarName = varMap.get(variableChild);
					if(mappedVarName!=null){
						variableChild.setVariableName(mappedVarName);
					}
				}
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}});
		
		return null;
	}
	
	private void processAllOperand(HAPOperand operand, Object data, HAPExpressionTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperand> children = operand.getChildren();
			for(HAPOperand child : children){
				this.processAllOperand(child, data, task);
			}
		}
	}
	
}
