package com.nosliw.core.application.common.dataexpression.definition;

import java.util.List;

import com.nosliw.core.xxx.application.common.dataexpression1.HAPInterfaceProcessOperand;


public class HAPUtilityOperandDefinition {

	static public void traverseAllOperand(HAPDefinitionOperand operand, Object data, HAPInterfaceProcessOperand task){
		if(task.processOperand(operand, data)){
			List<HAPDefinitionOperand> children = operand.getChildren();
			for(HAPDefinitionOperand child : children){
				HAPUtilityOperandDefinition.traverseAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
}
