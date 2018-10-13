package com.nosliw.data.core.task111.expression;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.task111.HAPDefinitionTask;

public class HAPExpressionTaskUtility {

	static public HAPDefinitionStep buildExpressionStep(Object obj, HAPManagerTaskExpression expTaskMan){
		HAPDefinitionStep step = null;
		try{
			JSONObject jsonObj = (JSONObject)obj;
			String type = jsonObj.optString(HAPDefinitionTask.TYPE);
			if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;
			step = expTaskMan.getStepManager(type).buildStepDefinition(obj);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return step;
	}
	
	
	
	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
//	static public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExpression> expressions, HAPResourceManagerRoot resourceMan) {
//		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
//		for(HAPExpression expression : expressions){
//			resourceIds.addAll(discoverResources(expression));
//		}
//		return resourceMan.discoverResources(new ArrayList<HAPResourceId>(resourceIds));
//	}

/*	
	static public List<HAPResourceId> discoverResources1(HAPExpression expression){
		Set<HAPResourceId> result = new LinkedHashSet<HAPResourceId>();
		//get converter resource id from var converter in expression 
		Map<String, HAPMatchers> matchers = expression.getVariableMatchers();
		if(matchers!=null){
			for(String varName : matchers.keySet()){
				Set<HAPDataTypeConverter> converters = HAPResourceUtility.getConverterResourceIdFromRelationship(matchers.get(varName).discoverRelationships());
				for(HAPDataTypeConverter converter : converters){
					result.add(new HAPResourceIdConverter(converter));
				}
			}
		}
		
		HAPOperandUtility.processAllOperand(expression.getOperand(), result, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<HAPResourceId> resourceIds = (Set<HAPResourceId>)data;
				
				resourceIds.addAll(operand.getOperand().getResources());
				
				
//				switch(operand.getOperand().getType()){
//				case HAPConstant.EXPRESSION_OPERAND_OPERATION:
//					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
//					HAPOperationId operationId = operationOperand.getOperationId();
//					//operation as resource
//					if(operationId!=null)	resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId));
//					break;
//				case HAPConstant.EXPRESSION_OPERAND_REFERENCE:
//					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
//					List<HAPResourceId> referenceResources = discoverResources(referenceOperand.getExpression());
//					resourceIds.addAll(referenceResources);
//					break;
//				}
//
//				//converter as resource
//				for(HAPDataTypeConverter converter : operand.getOperand().getConverters()){
//					resourceIds.add(new HAPResourceIdConverter(converter));
//				}
				
				return true;
			}
		});
		return new ArrayList(result);
	}	
*/	
}
