package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.List;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPManualUtilityOperand {

	static public void traverseAllOperand(HAPManualWrapperOperand operandWrapper, HAPManualHandlerOperand task, Object data){
		if(task.processOperand(operandWrapper, data)){
			List<HAPManualWrapperOperand> children = operandWrapper.getOperand().getChildren();
			for(HAPManualWrapperOperand child : children){
				traverseAllOperand(child, task, data);
			}
			task.postPross(operandWrapper, data);
		}
	}
	
	static public HAPContainerVariableInfo discover(
			List<HAPManualOperand> operands, 
			List<HAPDataTypeCriteria> expectOutputs,
			HAPContainerVariableInfo inVariablesInfo, 
			List<HAPMatchers> matchers,
			HAPDataTypeHelper dataTypeHelper,
			HAPProcessTracker processTracker) {
		//do discovery on operand
		HAPContainerVariableInfo varsInfo = inVariablesInfo.clone();
		
		HAPContainerVariableInfo oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			matchers.clear();
			oldVarsInfo = varsInfo;
			varsInfo = varsInfo.clone();
			processTracker.clear();
			for(int i=0; i<operands.size(); i++) {
				HAPDataTypeCriteria expectOutput = null;
				if(expectOutputs==null||expectOutputs.get(i)==null) {
					expectOutput = HAPDataTypeCriteriaAny.getCriteria(); 
				}
				
				matchers.add(operands.get(i).discover(varsInfo, expectOutput, processTracker, dataTypeHelper));
			}
		}while(!HAPUtilityBasic.isEqualMaps(varsInfo.getVariableCriteriaInfos(), oldVarsInfo.getVariableCriteriaInfos()) && processTracker.isSuccess());
		return varsInfo;
	}

	
}
