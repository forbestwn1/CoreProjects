package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;
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
	
	static public Set<HAPResourceId> discoverResources(HAPExpression expression){
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		
		//get converter resource id from var converter in expression 
		Map<String, HAPMatchers> matchers = expression.getVariableMatchers();
		if(matchers!=null){
			for(String varName : matchers.keySet()){
				Set<HAPDataTypeConverter> converters = HAPResourceUtility.getConverterResourceIdFromRelationship(matchers.get(varName).discoverRelationships());
				for(HAPDataTypeConverter converter : converters){
					out.add(new HAPResourceIdConverter(converter));
				}
			}
		}
		
		HAPOperandUtility.processAllOperand(expression.getOperand(), out, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<HAPResourceId> resourceIds = (Set<HAPResourceId>)data;
				switch(operand.getOperand().getType()){
				case HAPConstant.EXPRESSION_OPERAND_OPERATION:
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
					HAPOperationId operationId = operationOperand.getOperationId();
					//operation as resource
					if(operationId!=null)	resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId));
					break;
				case HAPConstant.EXPRESSION_OPERAND_REFERENCE:
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					Set<HAPResourceId> referenceResources = discoverResources(referenceOperand.getExpression());
					resourceIds.addAll(referenceResources);
					break;
				}

				//converter as resource
				for(HAPDataTypeConverter converter : operand.getOperand().getConverters()){
					resourceIds.add(new HAPResourceIdConverter(converter));
				}
				
				return true;
			}
		});
		return out;
	}	
}
