package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceInfo;
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
	
	
	  public static Map<String, HAPVariableInfo> buildVariablesInfoMapFromJson(JSONObject jsonObj) {
		  Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>(); 
		  if(jsonObj!=null) {
				Iterator<String> its = jsonObj.keys();
				while(its.hasNext()){
					String name = its.next();
					String criteriaStr = jsonObj.optString(name);
					HAPDataTypeCriteria criteria = HAPCriteriaUtility.parseCriteria(criteriaStr);
					out.put(name, new HAPVariableInfo(criteria));
				}
		  }
		  return out;
	  }

		//find all variables and references in expression
		public static Set<String> discoverVariables(HAPOperandWrapper operand) {
			Set<String> out = new HashSet<String>();
			HAPOperandUtility.processAllOperand(operand, null, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						out.add(variableOperand.getVariableName());
					}
					return true;
				}
			});		
			return out;
		}
		
		/**
		 * Discover resources required for expression 
		 * @param expression
		 * @return the reason the return type is list is because resource has sequence: some resource may need to load before another resoruce
		 */
		static public List<HAPResourceInfo> discoverResourceRequirement(List<HAPExecuteExpression> expressions, HAPResourceManagerRoot resourceMan) {
			List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
			for(HAPExecuteExpression expression : expressions){
				resourceIds.addAll(discoverResources(expression));
			}
			return resourceMan.discoverResources(new ArrayList<HAPResourceId>(resourceIds));
		}

		
		static public List<HAPResourceId> discoverResources(HAPExecuteExpression expression){
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
					return true;
				}
			});
			return new ArrayList(result);
		}	

}
