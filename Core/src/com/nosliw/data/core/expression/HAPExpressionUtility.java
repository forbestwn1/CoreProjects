package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceUtility;

public class HAPExpressionUtility {

	static public void processAllOperand(HAPOperand operand, Object data, HAPExpressionTask task){
		if(task.processOperand(operand, data)){
			List<HAPOperand> children = operand.getChildren();
			for(HAPOperand child : children){
				HAPExpressionUtility.processAllOperand(child, data, task);
			}
			task.postPross(operand, data);
		}
	}
	
	static public Set<HAPResourceId> discoverResources(HAPExpression expression){
		final Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		
		//get converter resource id from var converter in expression 
		Map<String, HAPConverters> converters = expression.getVariableConverters();
		for(String varName : converters.keySet()){
			for(HAPRelationship converter : converters.get(varName).getRelationships()){
				List<HAPResourceIdConverter> conerterIds = HAPResourceUtility.getConverterResourceIdFromRelationship(converter);
				out.addAll(conerterIds);
			}
		}
		
		processAllOperand(expression.getOperand(), out, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				switch(operand.getType()){
				case HAPConstant.EXPRESSION_OPERAND_OPERATION:
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand;
					HAPOperationId operationId = operationOperand.getOperationId();
					//operation as resource
					if(operationId!=null)	out.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId, null));
					break;
				case HAPConstant.EXPRESSION_OPERAND_REFERENCE:
					HAPOperandReference referenceOperand = (HAPOperandReference)operand;
					Set<HAPResourceId> referenceResources = discoverResources(referenceOperand.getExpression());
					out.addAll(referenceResources);
					break;
				}

				//converter as resource
				for(HAPRelationship converter : operand.getConverters()){
					List<HAPResourceIdConverter> conerterIds = HAPResourceUtility.getConverterResourceIdFromRelationship(converter);
					out.addAll(conerterIds);
				}
				
				return true;
			}

			@Override
			public void postPross(HAPOperand operand, Object data) {}
		});
		return out;
	}
	
}
