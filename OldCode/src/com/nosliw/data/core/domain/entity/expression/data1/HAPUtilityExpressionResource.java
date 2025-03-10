package com.nosliw.data.core.domain.entity.expression.data1;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPOperationId;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceIdExpression;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPUtilityExpressionResource {

	public static HAPResourceId buildResourceId(String suiteId, String expressionId) {
		return new HAPResourceIdExpression(new HAPIdExpressionGroup(suiteId, expressionId));
	}
	
	/**
	 * Discover resources required for expression 
	 * @param expression
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	static public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExecutableEntityExpressionDataGroup> expressions, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(HAPExecutableEntityExpressionDataGroup expression : expressions){
			resourceIds.addAll(discoverResources(expression, runtimeInfo, resourceMan));
		}
		return resourceMan.discoverResources(new ArrayList<HAPResourceId>(resourceIds), runtimeInfo);
	}
 
	static public List<HAPResourceIdSimple> discoverResources(HAPExecutableEntityExpressionDataGroup expressions, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager){
		Set<HAPResourceIdSimple> result = new LinkedHashSet<HAPResourceIdSimple>();
		Map<String, HAPExecutableExpressionData1> items = expressions.getAllExpressionItems();
		for(String name : items.keySet()) {
			HAPExecutableExpressionData1 expression = items.get(name);
			//get converter resource id from var converter in expression 
			HAPMatchers matchers = expression.getOutputMatchers();
			if(matchers!=null){
				result.addAll(HAPMatcherUtility.getMatchersResourceId(matchers));
			}
			
			HAPUtilityOperand.processAllOperand(expression.getOperand(), result, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					Set<HAPResourceIdSimple> resourceIds = (Set<HAPResourceIdSimple>)data;
					resourceIds.addAll(operand.getOperand().getResources(runtimeInfo, resourceManager));
					return true;
				}
			});
		}
		return new ArrayList(result);
	}	
	
	/**
	 * Discover resources required for data type operation
	 * @param dataTypeInfo
	 * @param dataOpInfo
	 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
	 */
	static public List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeId, String operation, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPOperationId operationId = new HAPOperationId(dataTypeId, operation);
		HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		resourceIds.add(resourceId);
		return resourceMan.discoverResources(resourceIds, runtimeInfo);
	}

	static public List<HAPResourceInfo> discoverResourceRequirement(HAPMatchers matchers, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		Set<HAPDataTypeConverter> converters = HAPMatcherUtility.getConverterResourceIdFromRelationship(matchers.discoverRelationships());
		for(HAPDataTypeConverter converter : converters){
			resourceIds.add(new HAPResourceIdConverter(converter));
		}
		return resourceMan.discoverResources(resourceIds, runtimeInfo);
	}
		
}
