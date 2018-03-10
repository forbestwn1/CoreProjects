package com.nosliw.uiresource.definition;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.expression.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpressionScriptSegment;
import com.nosliw.uiresource.expression.HAPScriptExpressionUtility;

public class HAPConstantUtility {

	/**
	 * Calculate all the constant values in ConstantDef
	 * @param parentConstants
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 */
	public static void calculateConstantDefs(
			HAPUIDefinitionUnit uiDefinitionUnit,
			Map<String, HAPConstantDef> parentConstants,
			HAPIdGenerator idGenerator, 
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		//build constants by merging parent with current
		Map<String, HAPConstantDef> contextConstants = new LinkedHashMap<String, HAPConstantDef>();
		if(parentConstants!=null)   contextConstants.putAll(parentConstants);
		contextConstants.putAll(uiDefinitionUnit.getConstantDefs());
		uiDefinitionUnit.setConstantDefs(contextConstants);
		
		//process all constants defined in this domain
		HAPConstantUtility.processConstantDefs(uiDefinitionUnit, idGenerator, expressionMan, runtime);
		
		//process constants in child
		for(HAPUIDefinitionUnitTag tag : uiDefinitionUnit.getUITags()){
			calculateConstantDefs(tag, contextConstants, idGenerator, expressionMan, runtime);
		}
	}
	
	/**
	 * process all constants defs to get data of constant
	 * it will keep the json structure and only calculate the leaf value 
	 * 		constantDefs : all available constants
	 */
	static public void processConstantDefs(
			HAPUIDefinitionUnit uiDefinitionUnit,
			HAPIdGenerator idGenerator, 
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		Map<String, HAPConstantDef> constantDefs = uiDefinitionUnit.getConstantDefs();
		for(String name : constantDefs.keySet()){
			HAPConstantDef constantDef = constantDefs.get(name);
			if(!constantDef.isProcessed()){
				processConstantDef(name, uiDefinitionUnit, idGenerator, expressionMan, runtime);
			}
			uiDefinitionUnit.addConstant(name, constantDef.getValue());
		}
	}

	static private void processConstantDef(
			String name, 
			HAPUIDefinitionUnit uiDefinitionUnit,
			HAPIdGenerator idGenerator, 
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		Map<String, HAPConstantDef> constantDefs = uiDefinitionUnit.getConstantDefs();
		HAPConstantDef constantDef = constantDefs.get(name);
		if(!constantDef.isProcessed()){
			//calculate the value
			Object data = processConstantDefJsonNode(constantDef.getDefinitionValue(), uiDefinitionUnit, idGenerator, expressionMan, runtime);
			if(data==null)   data = constantDef.getDefinitionValue();	
			constantDef.setValue(data);
			//add constant value to expression context if it is data value
			HAPData dataValue = constantDef.getDataValue();
			if(dataValue!=null)    uiDefinitionUnit.getExpressionContext().addConstant(name, dataValue);
			constantDef.processed();
		}
	}
	
	/**
	 * Process any json node
	 * @param node  
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 */
	static private Object processConstantDefJsonNode(
			Object node,
			HAPUIDefinitionUnit uiDefinitionUnit,
			HAPIdGenerator idGenerator, 
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		Map<String, HAPConstantDef> constantDefs = uiDefinitionUnit.getConstantDefs();
		Object out = null;
		try{
			if(node instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)node;
				Iterator<String> keys = jsonObj.keys();
				Map<String, Object> leafDatas = new LinkedHashMap<String, Object>();
				while(keys.hasNext()){
					String key = keys.next();
					Object childNode = jsonObj.get(key);
					Object data = processConstantDefJsonNode(childNode, uiDefinitionUnit, idGenerator, expressionMan, runtime);
					if(data!=null)   leafDatas.put(key, data);
				}
				for(String key : leafDatas.keySet()){
					jsonObj.remove(key);
					jsonObj.put(key, leafDatas.get(key));
				}
			}
			else if(node instanceof JSONArray){
				JSONArray jsonArray = (JSONArray)node;
				for(int i=0; i<jsonArray.length(); i++){
					Object childNode = jsonArray.get(i);
					Object data = processConstantDefJsonNode(childNode, uiDefinitionUnit, idGenerator, expressionMan, runtime);
					if(data!=null)   jsonArray.put(i, data);
				}
			}
			else{
				out = processConstantDefLeaf(node, uiDefinitionUnit, idGenerator, expressionMan, runtime);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Calculate leaf data
	 * as script expression can only defined in leaf node 
	 * @param leafData
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 * 		if leafData does not contain script expression, then return leafData (respect the value type)
	 * 		if leafData contains both script expression and plain text, then return string value
	 * 		if leafData contains a script expression only, then return the result value of sript expression
	 */
	static private Object processConstantDefLeaf(
			Object leafData,
			HAPUIDefinitionUnit uiDefinitionUnit,
			HAPIdGenerator idGenerator, 
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){

		Map<String, HAPConstantDef> constantDefs = uiDefinitionUnit.getConstantDefs();
		
		//if leafData is a script expression, then use valueObj to respect the type of return value
		Object valueObj = null;
		//otherwise, if leafData contains both script expression and plain text, then build string value instead
		StringBuffer valueStr = new StringBuffer();
		//try to discover script expression in leaf content 
		List<Object> segments = HAPScriptExpressionUtility.discoverUIExpressionInText(leafData.toString(), expressionMan);
		for(Object segment : segments){
			if(segment instanceof HAPScriptExpression){
				//only process script expression
				HAPScriptExpression sciptExpression = (HAPScriptExpression)segment;
				//find all required constants names in script expressions
				Set<String> expConstantNames = new HashSet<String>();
				Set<String> scriptConstantNames = new HashSet<String>();
				
				List<Object> uiExpEles = sciptExpression.getElements();
				for(Object uiExpEle : uiExpEles){
					if(uiExpEle instanceof HAPDefinitionExpression){
						HAPDefinitionExpression expDef = (HAPDefinitionExpression)uiExpEle;
						expConstantNames.addAll(HAPOperandUtility.discoveryUnsolvedConstants(expDef.getOperand()));
					}
					else if(uiExpEle instanceof HAPScriptExpressionScriptSegment){
						HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)uiExpEle;
						scriptConstantNames.addAll(scriptSegment.getConstantNames());
					}
				}
				
				//build constants data required by expression
				Map<String, HAPData> expParms = new LinkedHashMap<String, HAPData>();
				for(String constantName : expConstantNames){
					HAPConstantDef constantDef = constantDefs.get(constantName);
					processConstantDef(constantName, uiDefinitionUnit, idGenerator, expressionMan, runtime);
					HAPData parm = constantDef.getDataValue();
					expParms.put(constantName, parm);
				}
				
				//build constants required by script
				Map<String, Object> scriptConstants = new LinkedHashMap<String, Object>();
				for(String scriptConstantName : scriptConstantNames){
					HAPConstantDef constantDef = constantDefs.get(scriptConstantName);
					processConstantDef(scriptConstantName, uiDefinitionUnit, idGenerator, expressionMan, runtime);
					Object constant = constantDef.getValue();
					scriptConstants.put(scriptConstantName, constant);
				}
				
				//process expression in scriptExpression
				sciptExpression.processExpressions(uiDefinitionUnit.getExpressionContext(), HAPExpressionProcessConfigureUtil.setDoDiscovery(null));
				
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(sciptExpression, null, scriptConstants);
				HAPServiceData serviceData = runtime.executeTaskSync(task);
				
				if(segments.size()>1){
					//if have more than one segment, use the string value of result of script expression
					valueStr.append(serviceData.getData().toString());
				}
				else{
					//if have only one script expression, then use its object value
					valueObj = serviceData.getData();
				}
			}
			else{
				//build string value
				valueStr.append(segment.toString());
			}
		}
		
		if(valueObj!=null){
			//if only have one js expression
			return valueObj;
		}
		else if(segments.size()==1){
			//if no js expression, use the original one
			return leafData;
		}
		else{
			return valueStr.toString();
		}
	}

}
