package com.nosliw.data.core.operand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPOperandUtility {

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
	
}
