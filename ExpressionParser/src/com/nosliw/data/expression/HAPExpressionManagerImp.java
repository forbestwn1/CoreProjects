package com.nosliw.data.expression;

import java.util.List;
import java.beans.Expression;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
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
	public HAPExpressionInfo getExpressionInfo(String name) {		return this.m_expressionInfos.get(name);	}

	
	public HAPExpression processExpressionInfo(String expressionName) {
		HAPExpressionImp expression = this.buildExpression(expressionName);
		
		//process reference
		this.processReferences(expression.getOperand(), new LinkedHashMap<String, String>(), expression.getExpressionInfo());
		
		//process constant
		this.processConstants(expression.getOperand(), expression.getExpressionInfo());
		
		//discover variables
		Map<String, HAPDataTypeCriteria> expressionVars = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expressionVars.putAll(expression.getVariables());
		
		HAPProcessVariablesContext context = new HAPProcessVariablesContext();
		Map<String, HAPDataTypeCriteria> oldVars;
		//Do discovery util vars not change or fail 
		do{
			oldVars = new LinkedHashMap<String, HAPDataTypeCriteria>();
			oldVars.putAll(expressionVars);
			
			context.clear();
			expression.getOperand().discoverVariables(expressionVars, null, context);
		}while(!HAPBasicUtility.isEqualMaps(expressionVars, oldVars) && context.isSuccess());
		
		if(context.isSuccess()){
			expression.setVariables(expressionVars);
		}
		else{
			expression.setErrorMessage(context.getMessage());
		}
		return expression;
	}

	private HAPExpressionImp buildExpression(String expressionName){
		HAPExpressionInfo expressionInfo = getExpressionInfo(expressionName);
		HAPOperand expressionOperand = HAPExpressionParser.parseExpression(expressionInfo.getExpression());
		HAPExpressionImp expression = new HAPExpressionImp(expressionInfo, expressionOperand);
		return expression;
	}

	private void processConstants(HAPOperand operand, final HAPExpressionInfo expressionInfo){
		this.processAllOperand(operand, null, new HAPExpressionTask(){

			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					HAPExpression expression = reference.getExpression();
					processConstants(expression.getOperand(), expression.getExpressionInfo());
				}
				else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand;
					constantOperand.setData(expressionInfo.getConstants().get(constantOperand.getName()));
				}
				return false;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {
			}
		});		
	}
	
	private void processReferences(HAPOperand expressionOperand, Map<String, String> varsMapping, final HAPExpressionInfo expressionInfo) {
		this.processAllOperand(expressionOperand, varsMapping, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> varMap = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference reference = (HAPOperandReference)operand;
					String referenceName = reference.getExpressionName();
					HAPReferenceInfo referenceInfo = expressionInfo.getReferences().get(referenceName);
					
					HAPExpressionImp refExpression = buildExpression(referenceInfo.getReference());
					
					Map<String, String> refVarMap = referenceInfo.getVariableMap();

					//var mapping
					Map<String, String> childRefVarMap = new LinkedHashMap<String, String>();
					for(String r : refVarMap.keySet()){
						if(varMap.get(r)!=null)   childRefVarMap.put(r, varMap.get(r));
						else  childRefVarMap.put(r, refVarMap.get(r));
					}
					processReferences(refExpression.getOperand(), childRefVarMap, refExpression.getExpressionInfo());
					
					//variables mapping in var infos
					Map<String, HAPDataTypeCriteria> originalVarInfos = refExpression.getVariables();
					Map<String, HAPDataTypeCriteria> varInfos = new LinkedHashMap<String, HAPDataTypeCriteria>();
					for(String originalVarName : originalVarInfos.keySet()){
						String varName = refVarMap.get(originalVarName);
						if(varName==null)  varName = originalVarName;
						varInfos.put(varName, originalVarInfos.get(originalVarName));
					}
					refExpression.setVariables(varInfos);
					
					reference.setExpression(refExpression);
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

	private void processAllOperand(HAPOperand operand, Object data, HAPExpressionTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperand> children = operand.getChildren();
			for(HAPOperand child : children){
				this.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
}
