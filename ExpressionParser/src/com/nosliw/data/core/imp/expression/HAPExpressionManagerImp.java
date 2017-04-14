package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPExpressionTask;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private Map<String, HAPExpressionInfo> m_expressionInfos;

	private HAPExpressionParser m_expressionParser;
	
	private HAPDataTypeCriteriaManager m_criteriaMan;
	
	public HAPExpressionManagerImp(HAPExpressionParser expressionParser, HAPDataTypeCriteriaManager criteriaMan){
		this.m_expressionParser = expressionParser;
		this.m_criteriaMan = criteriaMan;
		this.init();
	}
	
	private void init(){
		String fileFolder = HAPFileUtility.getClassFolderPath(this.getClass()); 
		HAPValueInfoManager.getInstance().importFromFolder(fileFolder, false);

		this.m_expressionInfos = new LinkedHashMap<String, HAPExpressionInfo>();
	}

	@Override
	public HAPExpressionInfo getExpressionInfo(String name) {		return this.m_expressionInfos.get(name);	}

	@Override
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
			
			//normalize variable -- for every variable criteria, find root from data type
			//for operator, build convertor
			expression.buildNormalizedVariablesInfo();
		}
		else{
			expression.addErrorMessages(context.getMessages());
		}
		return expression;
	}

	
	private HAPExpressionImp buildExpression(String expressionName){
		HAPExpressionInfo expressionInfo = getExpressionInfo(expressionName);
		HAPOperand expressionOperand = this.getExpressionParser().parseExpression(expressionInfo.getExpression());
		HAPExpressionImp expression = new HAPExpressionImp(expressionInfo, expressionOperand);
		return expression;
	}

	private void processConstants(HAPOperand operand, final HAPExpressionInfo expressionInfo){
		HAPExpressionUtility.processAllOperand(operand, null, new HAPExpressionTask(){

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
		HAPExpressionUtility.processAllOperand(expressionOperand, varsMapping, new HAPExpressionTask(){
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
	
	protected HAPExpressionParser getExpressionParser(){		return this.m_expressionParser;	}
	protected HAPDataTypeCriteriaManager getCriteriaManager(){   return this.m_criteriaMan;   }
}
