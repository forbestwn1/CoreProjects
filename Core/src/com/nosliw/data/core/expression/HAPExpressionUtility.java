package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

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
	
	  public static Map<String, HAPVariableInfo> buildVariablesInfoMapFromJson(JSONObject jsonObj) {
		  Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>(); 
		  if(jsonObj!=null) {
				Iterator<String> its = jsonObj.keys();
				while(its.hasNext()){
					String name = its.next();
					String criteriaStr = jsonObj.optString(name);
					HAPDataTypeCriteria criteria = HAPCriteriaUtility.parseCriteria(criteriaStr);
					out.put(name, HAPVariableInfo.buildVariableInfo(criteria));
				}
		  }
		  return out;
	  }

		/**
		 * Discover resources required for expression 
		 * @param expression
		 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
		 */
		static public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExecutableExpression> expressions, HAPResourceManagerRoot resourceMan, HAPRuntimeInfo runtimeInfo) {
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			for(HAPExecutableExpression expression : expressions){
				resourceIds.addAll(discoverResources(expression));
			}
			return resourceMan.discoverResources(new ArrayList<HAPResourceId>(resourceIds), runtimeInfo);
		}

		static public List<HAPResourceIdSimple> discoverResources(HAPExecutableExpression expression){
			Set<HAPResourceIdSimple> result = new LinkedHashSet<HAPResourceIdSimple>();
			//get converter resource id from var converter in expression 
			Map<String, HAPMatchers> matchers = expression.getVariableMatchers();
			if(matchers!=null){
				for(String varName : matchers.keySet()){
					result.addAll(HAPMatcherUtility.getMatchersResourceId(matchers.get(varName)));
				}
			}
			
			HAPOperandUtility.processAllOperand(expression.getOperand(), result, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					Set<HAPResourceIdSimple> resourceIds = (Set<HAPResourceIdSimple>)data;
					resourceIds.addAll(operand.getOperand().getResources());
					return true;
				}
			});
			return new ArrayList(result);
		}	
		
		/**
		 * Discover resources required for data type operation
		 * @param dataTypeInfo
		 * @param dataOpInfo
		 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
		 */
		static public List<HAPResourceInfo> discoverResourceRequirement(HAPDataTypeId dataTypeId, String operation, HAPResourceManagerRoot resourceMan, HAPRuntimeInfo runtimeInfo) {
			HAPOperationId operationId = new HAPOperationId(dataTypeId, operation);
			HAPResourceId resourceId = new HAPResourceIdOperation(operationId);
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			resourceIds.add(resourceId);
			return resourceMan.discoverResources(resourceIds, runtimeInfo);
		}

		static public List<HAPResourceInfo> discoverResourceRequirement(HAPMatchers matchers, HAPResourceManagerRoot resourceMan, HAPRuntimeInfo runtimeInfo) {
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			Set<HAPDataTypeConverter> converters = HAPMatcherUtility.getConverterResourceIdFromRelationship(matchers.discoverRelationships());
			for(HAPDataTypeConverter converter : converters){
				resourceIds.add(new HAPResourceIdConverter(converter));
			}
			return resourceMan.discoverResources(resourceIds, runtimeInfo);
		}
		
		/**
		 * Discover resources required for expression 
		 * @param expression
		 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
		 */
		static private List<HAPResourceIdSimple> getResourceRequirement(HAPExecutableExpression expression){
			Set<HAPResourceIdSimple> result = new LinkedHashSet<HAPResourceIdSimple>();
			HAPOperandUtility.processAllOperand(expression.getOperand(), result, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					Set<HAPResourceIdSimple> resourceIds = (Set<HAPResourceIdSimple>)data;
					resourceIds.addAll(operand.getOperand().getResources());
					return true;
				}
			});
			return new ArrayList(result);
		}	
		
}
