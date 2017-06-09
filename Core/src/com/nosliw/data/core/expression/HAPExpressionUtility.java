package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceUtility;

public class HAPExpressionUtility {

	static public String buildFullVariableName(String parent, String name){
		String out = HAPNamingConversionUtility.cascadePath(parent, name); 
		return out;
	}
	
	static public String updateVaraible(String parent, String name){
		String out = name;
		if(!out.startsWith(name+HAPConstant.SEPERATOR_PATH)){
			out = buildFullVariableName(parent, name);
		}
		return out;
	}
	
	static public Set<String> discoveryVariables(HAPOperand operand){
		Set<String> out = new HashSet<String>();
		processAllOperand(operand, out, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Set<String> vars = (Set<String>)data;
				switch(operand.getType()){
				case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
					HAPOperandVariable varOperand = (HAPOperandVariable)operand;
					vars.add(varOperand.getVariableName());
					break;
				}
				return true;
			}
		});
		return out;
	}
	
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
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		
		//get converter resource id from var converter in expression 
		Map<String, HAPMatchers> matchers = expression.getVariableMatchers();
		for(String varName : matchers.keySet()){
			Set<HAPDataTypeConverter> converters = HAPResourceUtility.getConverterResourceIdFromRelationship(matchers.get(varName).getRelationships());
			for(HAPDataTypeConverter converter : converters){
				out.add(new HAPResourceIdConverter(converter));
			}
		}
		
		processAllOperand(expression.getOperand(), out, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Set<HAPResourceId> resourceIds = (Set<HAPResourceId>)data;
				switch(operand.getType()){
				case HAPConstant.EXPRESSION_OPERAND_OPERATION:
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand;
					HAPOperationId operationId = operationOperand.getOperationId();
					//operation as resource
					if(operationId!=null)	resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId));
					break;
				case HAPConstant.EXPRESSION_OPERAND_REFERENCE:
					HAPOperandReference referenceOperand = (HAPOperandReference)operand;
					Set<HAPResourceId> referenceResources = discoverResources(referenceOperand.getExpression());
					resourceIds.addAll(referenceResources);
					break;
				}

				//converter as resource
				for(HAPDataTypeConverter converter : operand.getConverters()){
					resourceIds.add(new HAPResourceIdConverter(converter));
				}
				
				return true;
			}
		});
		return out;
	}	
}
