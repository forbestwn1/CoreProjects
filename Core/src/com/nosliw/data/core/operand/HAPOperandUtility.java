package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.task111.HAPExecutableTask;

public class HAPOperandUtility {

	static public void replaceAttributeOpWithOperationOp(HAPOperandWrapper operand) {
		List<HAPOperandWrapper> attrOperands = new ArrayList<HAPOperandWrapper>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public void postPross(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					attrOperands.add(operand);
				}
			}

			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {		return true;	}
		});	

		for(int i=0; i<attrOperands.size(); i++) {
			HAPOperandWrapper attrOpWrapper = attrOperands.get(i);
			HAPOperandAttribute attrOp = (HAPOperandAttribute)attrOpWrapper.getOperand();
			
			List<HAPParmInOperationOperand> parms = new ArrayList<HAPParmInOperationOperand>();
			parms.add(new HAPParmInOperationOperand(HAPConstant.DATAOPERATION_COMPLEX_GETCHILDDATA_NAME, new HAPOperandConstant(new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), attrOp.getAttribute()))));
			HAPOperandOperation opOperand = new HAPOperandOperation(attrOp.getBase().getOperand(), HAPConstant.DATAOPERATION_COMPLEX_GETCHILDDATA, parms);
			attrOpWrapper.setOperand(opOperand);
		}
	}
	
	static public void updateReferencedExecute(HAPOperandWrapper operand, Map<String, HAPExecutableTask> references) {
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					referenceOperand.updateReferenceExecute(references);
				}
				return true;
			}
		});	
	}
	
	static public void updateVariableName(HAPOperandWrapper operand, HAPUpdateName updateVar) {
		//update variable operand
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
					String newName = updateVar.getUpdatedName(variableChild.getVariableName()); 
					if(newName!=null)	variableChild.setVariableName(newName);
				}
				return true;
			}
		});	
	}

	static public void updateConstantName(HAPOperandWrapper operand, HAPUpdateName updateVar) {
		//update variable operand
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
					HAPOperandConstant constantChild = (HAPOperandConstant)operand.getOperand();
					if(HAPBasicUtility.isStringNotEmpty(constantChild.getName())){
						String newName = updateVar.getUpdatedName(constantChild.getName()); 
						constantChild.setName(newName);
					}
				}
				return true;
			}
		});	
	}

	static public Set<String> discoverReferences(HAPOperandWrapper operand) {
		Set<String> out = new HashSet<String>();
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_REFERENCE)){
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					out.add(referenceOperand.getReferenceName());
				}
				return true;
			}
		});	
		return out;
	}
	
	
	static public Set<String> discoverVariables(HAPOperandWrapper operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<String> vars = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
					HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
					vars.add(varOperand.getVariableName());
					break;
				}
				return true;
			}
		});
		return out;
	}
	
	/**
	 * Find all the unsolved constants names: constants that only provide name
	 * @param operand
	 * @return
	 */
	static public Set<String> discoveryUnsolvedConstants(HAPOperandWrapper operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<String> vars = (Set<String>)data;
				switch(operand.getOperand().getType()){
				case HAPConstant.EXPRESSION_OPERAND_CONSTANT:
					HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
					if(constantOperand.getData()==null){
						vars.add(constantOperand.getName());
					}
					break;
				}
				return true;
			}
		});
		return out;
	}
	
	static public void processAllOperand(HAPOperandWrapper operand, Object data, HAPOperandTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperandWrapper> children = operand.getOperand().getChildren();
			for(HAPOperandWrapper child : children){
				HAPOperandUtility.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
	static public void updateConstantData(HAPOperandWrapper operand, final Map<String, HAPData> contextConstants){
		if(contextConstants!=null && !contextConstants.isEmpty()) {
			HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstant.EXPRESSION_OPERAND_CONSTANT)){
						HAPOperandConstant constantOperand = (HAPOperandConstant)operand.getOperand();
						if(constantOperand.getData()==null){
							String constantName = constantOperand.getName();
							HAPData constantData = contextConstants.get(constantName);
							constantOperand.setData(constantData);
						}
					}
					else if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable varOperand = (HAPOperandVariable)operand.getOperand();
						HAPData cstData = contextConstants.get(varOperand.getVariableName());
						if(cstData!=null) {
							//find constant value for variable, replace with constant
							HAPOperandConstant constantOperand = new HAPOperandConstant(cstData);
							operand.setOperand(constantOperand);
						}
					}
					return true;
				}
			});	
		}
	}

	static public Map<String, HAPVariableInfo> discover(
			HAPOperand[] operands, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			HAPDataTypeCriteria expectOutput,
			HAPProcessTracker processTracker) {
		//do discovery on operand
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(parentVariablesInfo);
		
		Map<String, HAPVariableInfo> oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			processTracker.clear();
			for(HAPOperand operand : operands) {
				operand.discover(varsInfo, expectOutput, processTracker, HAPExpressionManager.dataTypeHelper);
			}
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && processTracker.isSuccess());
		return varsInfo;
	}

}
