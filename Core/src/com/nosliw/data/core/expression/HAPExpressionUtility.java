package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
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
	
	/**
	 * Discover resources required for data type operation
	 * @param dataTypeInfo
	 * @param dataOpInfo
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	static public List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeId, HAPOperation dataOpInfo, HAPResourceManagerRoot resourceMan) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, dataOpInfo.getName());
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return resourceMan.discoverResources(resourceIds);
	}

	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	static public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExpression> expressions, HAPResourceManagerRoot resourceMan) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(HAPExpression expression : expressions){
			resourceIds.addAll(discoverResources(expression));
		}
		return resourceMan.discoverResources(new ArrayList<HAPResourceId>(resourceIds));
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
