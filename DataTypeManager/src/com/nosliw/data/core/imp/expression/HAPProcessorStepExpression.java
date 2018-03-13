package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperationParmInfo;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandOperation;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.expression.HAPProcessorStep;
import com.nosliw.data.core.task.expression.HAPReferenceInfo;
import com.nosliw.data.core.task.expression.HAPDefinitionStep;
import com.nosliw.data.core.task.expression.HAPDefinitionStepExpression;
import com.nosliw.data.core.task.expression.HAPExecutableStepExpression;
import com.nosliw.data.core.task.expression.HAPExpressionTaskUtility;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPProcessorStepExpression implements HAPProcessorStep{

	private HAPExpressionParser m_expressionParser;
	
	private HAPDataTypeHelper m_dataTypeHelper;

	public HAPProcessorStepExpression(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
		HAPManagerTask.expressionParser = expressionParser;
		this.m_dataTypeHelper = dataTypeHelper;
	}
	
	@Override
	public HAPExecutable process(
			HAPDefinitionStep stepDefinition,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			Map<String, HAPVariableInfo> contextVariableCriterias,
			HAPProcessExpressionDefinitionContext context
	) {
		HAPDefinitionStepExpression stepDefExp = (HAPDefinitionStepExpression)stepDefinition;
		HAPExecutableStepExpression out = new HAPExecutableStepExpression(stepDefExp);

		//process reference
		System.out.println("******* Process reference");
		this.processReference(expression, null, contextExpressionDefinitions);
		
		//process constant
		System.out.println("******* Process constant");
		this.processConstants(expression, contextConstants);

		//process anonomouse parameter in operaion
		this.processDefaultAnonomousParmInOperation(expression);
		
		//only discovery is configure as "true"
		if(HAPExpressionProcessConfigureUtil.isDoDiscovery(context)){
			//discover variables criteria / matchs in expression
			Map<String, HAPVariableInfo> parentVariableInfos = new LinkedHashMap<String, HAPVariableInfo>();
			if(variableCriterias!=null){
				for(String varName : variableCriterias.keySet())		parentVariableInfos.put(varName, new HAPVariableInfo(variableCriterias.get(varName)));
			}
			expression.discoverVariable(parentVariableInfos, null, context, this.getDataTypeHelper());
		}
		
		if(!context.isSuccess()){
			System.out.println("******* Error");
			expression.addErrorMessages(context.getMessages());
		}
		else{
			System.out.println(expression);
			System.out.println("******* End");
		}
		return expression;

	}

	
	/**
	 * Process all references in expression
	 * Update variables in expression
	 * All the operation above also operate on child referenced expression
	 */
	private void processReference(final HAPExpressionImp expression, Map<String, String> varMapping, final Map<String, HAPDefinitionTask> contextExpressionDefinitions){
		//process all child references
		HAPOperandUtility.processAllOperand(expression.getOperand(), null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					String referenceName = referenceOperand.getReferenceName();
					HAPReferenceInfo referenceInfo = expression.getExpressionDefinition().getReferences().get(referenceName);
					
					String refExpName = null;   //referenced expression name, by default, use referenceName as expression name
					if(referenceInfo!=null)		refExpName = referenceInfo.getReference();
					if(refExpName==null)  refExpName = referenceName;
					
					HAPExpressionImp refExpression = (HAPExpressionImp)expression.getReference(refExpName);
					if(refExpression==null){
						//if referenced expression has not been processed, parse it
						refExpression = parseExpressionDefinition(contextExpressionDefinitions.get(refExpName));
						discoverLocalVariables(refExpression);
						expression.addReference(referenceName, refExpression);
						
						Map<String, String> refVarMap = new LinkedHashMap<String, String>();   //variable mapping from parent to reference expression
						if(referenceInfo!=null){
							refVarMap = referenceInfo.getVariablesMap();
						}
						
						processReference(refExpression, HAPBasicUtility.reverseMapping(refVarMap), contextExpressionDefinitions);
					}
					referenceOperand.setExpression(refExpression);
				}
				return true;
			}
		});		
		
		//update variables in current expression
		if(varMapping!=null){
			expression.updateVariablesName(varMapping);
		}
	}
	
	

	
	private void processConstants(final HAPExpression expression, final Map<String, HAPData> contextConstants){
		HAPOperandUtility.processAllOperand(expression.getOperand(), expression.getExpressionDefinition(), new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null){
						HAPDefinitionTask expressionDefinition = (HAPDefinitionTask)data;
						String constantName = constantOperand.getName();
						HAPData constantData = expressionDefinition.getConstants().get(constantName);
						if(constantData==null){
							//if not found within expression definition, try to find from context
							constantData = contextConstants.get(constantName);
						}
						constantOperand.setData(constantData);
					}
				}
				return true;
			}
		});	
		
		//process constant in referenced expression
		Map<String, HAPExpression> references = expression.getReferences();
		for(String referenceName : references.keySet()){
			HAPExpressionImp refExpression = (HAPExpressionImp)references.get(referenceName);
			processConstants(refExpression, contextConstants);
		}
	}
	
	protected HAPExpressionParser getExpressionParser(){		return this.m_expressionParser;	}
	protected HAPDataTypeHelper getDataTypeHelper(){   return this.m_dataTypeHelper;   }

}
