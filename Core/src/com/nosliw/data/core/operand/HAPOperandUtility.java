package com.nosliw.data.core.operand;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPOperandUtility {

	static public void updateVariable(HAPOperandWrapper operand, HAPUpdateVariable updateVar) {
		//update variable operand
		HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
					String newName = updateVar.getUpdatedVariable(variableChild.getVariableName()); 
					if(newName!=null)	variableChild.setVariableName(newName);
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
	
	
	static public Set<String> discoveryVariables(HAPOperandWrapper operand){
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
					return true;
				}
			});	
		}
	}

	static public Map<String, HAPVariableInfo> discover(
			HAPOperand operand, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			HAPDataTypeCriteria expectOutput,
			HAPProcessContext context) {
		//do discovery on operand
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(parentVariablesInfo);
		
		Map<String, HAPVariableInfo> oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			context.clear();
			operand.discover(varsInfo, expectOutput, context, HAPExpressionManager.dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());
		return varsInfo;
	}

//	static public Map<String, HAPMatchers> aaa(){
//		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
//		
//		//merge back, cal variable matchers, update parent variable
//		for(String varName : varsInfo.keySet()){
//			HAPVariableInfo varInfo = varsInfo.get(varName);
//			HAPVariableInfo parentVarInfo = parentVariablesInfo.get(varName);
//			if(parentVarInfo==null){
//				parentVarInfo = new HAPVariableInfo();
//				parentVarInfo.setCriteria(varInfo.getCriteria());
//				parentVariablesInfo.put(varName, parentVarInfo);
//			}
//			else{
//				if(parentVarInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
//					HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
//					parentVarInfo.setCriteria(adjustedCriteria);
//				}
//			}
//
//			//cal var converters
//			HAPMatchers varMatchers = dataTypeHelper.buildMatchers(parentVarInfo.getCriteria(), varInfo.getCriteria());
//			matchers.put(varName, varMatchers);
//		}
//		
//		return matchers;
//		
//	}
	
}
