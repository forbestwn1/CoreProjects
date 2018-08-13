package com.nosliw.data.core.script.constant;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPContextExpressionProcess;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionScriptSegment;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;

public class HAPConstantUtility {

	/**
	 * process all constants defs to get data of constant
	 * it will keep the json structure and only calculate the leaf value 
	 * 		constantDefs : all available constants
	 */
	static public void processConstantDefs(
			Map<String, HAPConstantDef> constantDefs,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		for(String name : constantDefs.keySet()){
			HAPConstantDef constantDef = constantDefs.get(name);
			if(!constantDef.isProcessed()){
				processConstantDef(name, constantDefs, expressionMan, runtime);
			}
		}
	}

	static private HAPConstantDef processConstantDef(
			String name, 
			Map<String, HAPConstantDef> constantDefs,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		HAPConstantDef constantDef = constantDefs.get(name);
		if(!constantDef.isProcessed()){
			//calculate the value
			Object data = processConstantDefJsonNode(name, constantDef.getDefinitionValue(), constantDefs, expressionMan, runtime);
			if(data==null)   data = constantDef.getDefinitionValue();	
			constantDef.setValue(data);
			constantDef.processed();
		}
		return constantDef;
	}
	
	/**
	 * Process any json node
	 * @param node  
	 * @param refConstants   reference constants
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 */
	static private Object processConstantDefJsonNode(
			String constantName,
			Object node,
			Map<String, HAPConstantDef> constantDefs,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		Object out = null;
		try{
			if(node instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)node;
				Iterator<String> keys = jsonObj.keys();
				Map<String, Object> leafDatas = new LinkedHashMap<String, Object>();
				while(keys.hasNext()){
					String key = keys.next();
					Object childNode = jsonObj.get(key);
					Object data = processConstantDefJsonNode(constantName, childNode, constantDefs, expressionMan, runtime);
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
					Object data = processConstantDefJsonNode(constantName, childNode, constantDefs, expressionMan, runtime);
					if(data!=null)   jsonArray.put(i, data);
				}
			}
			else{
				out = processConstantDefLeaf(constantName, node, constantDefs, expressionMan, runtime);
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
			String cstName,
			Object leafData,
			Map<String, HAPConstantDef> constantDefs,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){

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
				for(Object uiExpEle : sciptExpression.getElements()){
					if(uiExpEle instanceof HAPDefinitionExpression)		expConstantNames.addAll(HAPOperandUtility.discoveryUnsolvedConstants(((HAPDefinitionExpression)uiExpEle).getOperand()));
					else if(uiExpEle instanceof HAPScriptExpressionScriptSegment)		scriptConstantNames.addAll(((HAPScriptExpressionScriptSegment)uiExpEle).getConstantNames());
				}
				
				//build constants data required by expression
				HAPContextExpressionProcess expProcessContext = new HAPContextExpressionProcess();
				for(String constantName : expConstantNames){
					HAPConstantDef constantDef = processConstantDef(constantName, constantDefs, expressionMan, runtime);
					expProcessContext.addConstant(constantName, constantDef.getDataValue());
					constantDef.addReferenced(constantName);
				}
				
				//build constants required by script
				Map<String, Object> scriptConstants = new LinkedHashMap<String, Object>();
				for(String scriptConstantName : scriptConstantNames){
					HAPConstantDef constantDef = processConstantDef(scriptConstantName, constantDefs, expressionMan, runtime);
					scriptConstants.put(scriptConstantName, constantDef.getValue());
					constantDef.addReferenced(scriptConstantName);
				}
				
				//process expression in scriptExpression
				sciptExpression.processExpressions(expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));
				
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
