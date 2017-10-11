package com.nosliw.uiresource;

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
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.expression.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpressionScriptSegment;
import com.nosliw.uiresource.expression.HAPScriptExpressionUtility;

public class HAPConstantUtility {

	/**
	 * process all constants defs to get data of constant
	 * it will keep the json structure and only calculate the leaf value 
	 * 		constantDefs : all available constants
	 */
	static public void processConstantDefs(
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		for(String name : constantDefs.keySet()){
			HAPConstantDef constantDef = constantDefs.get(name);
			processConstantDef(constantDef, constantDefs, idGenerator, expressionMan, runtime);
			if(!constantDef.isProcessed()){
				Object data = processConstantDefJsonNode(constantDef.getDefinitionValue(), constantDefs, idGenerator, expressionMan, runtime);
				if(data==null)   constantDef.setValue(constantDef.getDefinitionValue());	
				else		constantDef.setValue(data);
				constantDef.processed();
			}
		}
	}

	static private void processConstantDef(
			HAPConstantDef constantDef,
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		if(!constantDef.isProcessed()){
			Object data = processConstantDefJsonNode(constantDef.getDefinitionValue(), constantDefs, idGenerator, expressionMan, runtime);
			if(data==null)   constantDef.setValue(constantDef.getDefinitionValue());	
			else		constantDef.setValue(data);
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
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
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
					Object data = processConstantDefJsonNode(childNode, constantDefs, idGenerator, expressionMan, runtime);
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
					Object data = processConstantDefJsonNode(childNode, constantDefs, idGenerator, expressionMan, runtime);
					if(data!=null)   jsonArray.put(i, data);
				}
			}
			else{
				out = processConstantDefLeaf(node, constantDefs, idGenerator, expressionMan, runtime);
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
	static Object processConstantDefLeaf(
			Object leafData,
			Map<String, HAPConstantDef> constantDefs,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		
		//if leafData is a script expression, then use valueObj to respect the type of return value
		Object valueObj = null;
		//otherwise, if leafData contains both script expression and plain text, then build string value instead
		StringBuffer valueStr = new StringBuffer();
		
		//try to discover script expression in leaf content 
		List<Object> segments = HAPScriptExpressionUtility.discoverUIExpressionInText(leafData.toString(), idGenerator, expressionMan);
		for(Object segment : segments){
			if(segment instanceof HAPScriptExpression){
				//only process script expression
				HAPScriptExpression sciptExpression = (HAPScriptExpression)segment;
				//find all required constants names in script expressions
				Set<String> expConstantNames = new HashSet<String>();
				Set<String> scriptConstantNames = new HashSet<String>();
				
				List<Object> uiExpEles = sciptExpression.getElements();
				for(Object uiExpEle : uiExpEles){
					if(uiExpEle instanceof HAPExpressionDefinition){
						HAPExpressionDefinition expDef = (HAPExpressionDefinition)uiExpEle;
						HAPOperand expOperand = expDef.getOperand();
						expConstantNames.addAll(HAPExpressionUtility.discoveryUnsolvedConstants(expOperand));
					}
					else if(uiExpEle instanceof HAPScriptExpressionScriptSegment){
						HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)uiExpEle;
						scriptConstantNames.addAll(scriptSegment.getConstants());
					}
				}
				
				//build constants data required by expression
				Map<String, HAPData> expParms = new LinkedHashMap<String, HAPData>();
				for(String constantName : expConstantNames){
					HAPConstantDef constantDef = constantDefs.get(constantName);
					processConstantDef(constantDef, constantDefs, idGenerator, expressionMan, runtime);
					HAPData parm = constantDef.getDataValue();
					expParms.put(constantName, parm);
				}
				
				//build constants required by script
				Map<String, Object> scriptConstants = new LinkedHashMap<String, Object>();
				for(String scriptConstantName : scriptConstantNames){
					HAPConstantDef constantDef = constantDefs.get(scriptConstantName);
					processConstantDef(constantDef, constantDefs, idGenerator, expressionMan, runtime);
					Object constant = constantDef.getValue();
					scriptConstants.put(scriptConstantName, constant);
				}
				
				//process expression in scriptExpression
				Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
				for(Object ele : sciptExpression.getElements()){
					if(ele instanceof HAPExpressionDefinition){
						HAPExpressionDefinition expDef = (HAPExpressionDefinition)ele;
						HAPExpression expression = expressionMan.processExpression(expDef.getName(), expDef, expParms, null);
						expressions.put(expDef.getName(), expression);
					}
				}
				
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(sciptExpression, expressions, null, scriptConstants);
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
