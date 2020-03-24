package com.nosliw.data.core.script.context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpression;
import com.nosliw.data.core.script.expression.HAPContextProcessScriptExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionEmbededScriptExpression;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptInScriptExpression;

public class HAPProcessorContextConstant {

	static public HAPContextGroup process(
			HAPContextGroup originalContextGroup,
			HAPParentContext parent,
			String inheritMode,
			HAPRequirementContextProcessor contextProcessRequirement){

		//merge with parent
		HAPContextGroup merged = originalContextGroup;
		for(String parentName : parent.getNames()) {
			merged = mergeWithParent(merged, (HAPContextGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedContext(parentName, parent, merged), false), inheritMode);
		}
		
		//figure out constant value (some constant may use another constant)
		HAPContextGroup out =  solidateConstantDefs(merged, contextProcessRequirement);
		
		//figure out root that ture out to be constant value, then convert to constant root
		out = discoverConstantContextRoot(out);
		
		return out;
	}

	//merge constant with parent
	//child constant has higher priority than parent
	private static HAPContextGroup mergeWithParent(
			HAPContextGroup contextGroup,
			HAPContextGroup parentContextGroup,
			String inheritMode){
		HAPContextGroup out = contextGroup.cloneContextGroup();
		if(!HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE.equals(inheritMode)) {
			if(parentContextGroup!=null) {
				//merge constants with parent
				for(String contextCategary : HAPContextGroup.getInheritableContextTypes()) {
					for(String name : parentContextGroup.getContext(contextCategary).getElementNames()) {
						if(parentContextGroup.getElement(contextCategary, name).isConstant()) {
							if(contextGroup.getElement(contextCategary, name)==null) {
								out.addElement(name, parentContextGroup.getElement(contextCategary, name).cloneContextDefinitionRoot(), contextCategary);
							}
						}
					}
				}
			}
		}
		return out;
	}

	//find all the context root which is actually constant, convert it to constant element 
	static private HAPContextGroup discoverConstantContextRoot(HAPContextGroup contextGroup) {
		for(String contextType : contextGroup.getContextTypes()) {
			HAPContext context = contextGroup.getContext(contextType);
			for(String eleName : context.getElementNames()) {
				HAPContextDefinitionRoot contextRoot = context.getElement(eleName);
				HAPContextDefinitionElement ele = contextRoot.getDefinition();
				if (HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(ele.getType())) {
					Object value = discoverConstantValue(ele);
					if(value!=null) {
						HAPContextDefinitionLeafConstant constantEle = new HAPContextDefinitionLeafConstant();
						constantEle.setValue(value);
						contextRoot.setDefinition(constantEle);
					}
				}
			}
		}
		return contextGroup;
	}
	
	static private Object discoverConstantValue(HAPContextDefinitionElement contextDefEle) {
		String type = contextDefEle.getType();
		if(HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(type)) {
			HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		else if (HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(type)) {
			HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
			JSONObject out = new JSONObject();
			for(String nodeName : nodeEle.getChildren().keySet()) {
				Object childOut = discoverConstantValue(nodeEle.getChild(nodeName));
				if(childOut==null)   return null;
				else   out.put(nodeName, childOut);
			}
			return out;
		}
		else return null;
	}
	
	/**
	 * process all constants defs to get data of constant
	 * it will keep the json structure and only calculate the leaf value 
	 * 		constantDefs : all available constants
	 */
	static private HAPContextGroup solidateConstantDefs(
			HAPContextGroup contextGroup,
			HAPRequirementContextProcessor contextProcessRequirement){
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPContextGroup.getAllContextTypes()) {
			Map<String, HAPContextDefinitionRoot> cotextDefRoots = out.getElements(categary);
			for(String name : cotextDefRoots.keySet()) {
				HAPContextDefinitionRoot contextDefRoot = cotextDefRoots.get(name);
				HAPUtilityContext.processContextDefElement(contextDefRoot.getDefinition(), new HAPContextDefEleProcessor() {
					@Override
					public boolean process(HAPContextDefinitionElement ele, Object value) {
						if(HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(ele.getType())) {
							solidateConstantDefEle((HAPContextDefinitionLeafConstant)ele, contextGroup, contextProcessRequirement);
						}
						return true;
					}

					@Override
					public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
						return true;
					}}, null);
			}
		}
		return out;
	}

	
	static private void solidateConstantDefEle(
			HAPContextDefinitionLeafConstant contextDefConstant, 
			HAPContextGroup contextGroup,
			HAPRequirementContextProcessor contextProcessRequirement) {

		if(!contextDefConstant.isProcessed()) {
			Object data = processConstantDefJsonNode(contextDefConstant.getValue(), contextGroup, contextProcessRequirement);
			if(data==null)   data = contextDefConstant.getValue();
			contextDefConstant.setValue(data);
		}
	}

	/**
	 * Process any json node
	 * @param nodeValue  
	 * @param refConstants   reference constants
	 * @param constantDefs
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 * @return
	 */
	static private Object processConstantDefJsonNode(
			Object nodeValue,
			HAPContextGroup contextGroup,
			HAPRequirementContextProcessor contextProcessRequirement) {
		Object out = null;
		try{
			if(nodeValue instanceof JSONObject){
				JSONObject outJsonObj = new JSONObject();
				JSONObject jsonObj = (JSONObject)nodeValue;
				Iterator<String> keys = jsonObj.keys();
				while(keys.hasNext()){
					String key = keys.next();
					Object childValue = jsonObj.get(key);
					Object data = processConstantDefJsonNode(childValue, contextGroup, contextProcessRequirement);
					if(data!=null)  outJsonObj.put(key, data);   
				}
				out = outJsonObj;
			}
			else if(nodeValue instanceof JSONArray){
				JSONArray outJsonArray = new JSONArray();
				JSONArray jsonArray = (JSONArray)nodeValue;
				for(int i=0; i<jsonArray.length(); i++){
					Object childNode = jsonArray.get(i);
					Object data = processConstantDefJsonNode(childNode, contextGroup, contextProcessRequirement);
					if(data!=null)   outJsonArray.put(i, data);
				}
				out = outJsonArray;
			}
			else{
				out = processConstantDefLeaf(nodeValue, contextGroup, contextProcessRequirement);
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
			HAPContextGroup contextGroup,
			HAPRequirementContextProcessor contextProcessRequirement) {

		//if leafData is a script expression, then use valueObj to respect the type of return value
		Object valueObj = null;
		//otherwise, if leafData contains both script expression and plain text, then build string value instead
		StringBuffer valueStr = new StringBuffer();
		//try to discover script expression in leaf content
		HAPDefinitionEmbededScriptExpression embededScriptExpDef = new HAPDefinitionEmbededScriptExpression(leafData.toString());
		List<Object> segments = embededScriptExpDef.getElements();
		for(Object segment : segments){
			if(segment instanceof HAPDefinitionScriptExpression){
				//only process script expression
				HAPDefinitionScriptExpression sciptExpressionDef = (HAPDefinitionScriptExpression)segment;

				//find all required constants names in script expressions
				Set<String> expConstantNames = new HashSet<String>();
				Set<String> scriptConstantNames = new HashSet<String>();
				for(Object uiExpEle : sciptExpressionDef.getElements()){
					if(uiExpEle instanceof HAPDefinitionExpression)		expConstantNames.addAll(HAPOperandUtility.discoveryUnsolvedConstants(((HAPDefinitionExpression)uiExpEle).getExpression().getOperand()));
					else if(uiExpEle instanceof HAPScriptInScriptExpression)		scriptConstantNames.addAll(((HAPScriptInScriptExpression)uiExpEle).getConstantNames());
				}
				
				//build constants data required by expression
				HAPContextProcessScriptExpression expProcessContext = new HAPContextProcessScriptExpression();
				for(String constantName : expConstantNames){
					HAPContextDefinitionRootId refNodeId = solveReferencedNodeId(new HAPContextDefinitionRootId(constantName), contextGroup);
					HAPContextDefinitionLeafConstant refContextDefEle = (HAPContextDefinitionLeafConstant)HAPUtilityContext.getDescendant(contextGroup, refNodeId.getCategary(), refNodeId.getName());
					solidateConstantDefEle(refContextDefEle, contextGroup, contextProcessRequirement);
					expProcessContext.addConstant(constantName, refContextDefEle.getDataValue());
				}
				
				//build constants required by script
//				Map<String, Object> scriptConstants = new LinkedHashMap<String, Object>();
				for(String scriptConstantName : scriptConstantNames){
					HAPContextDefinitionRootId refNodeId = solveReferencedNodeId(new HAPContextDefinitionRootId(scriptConstantName), contextGroup);
					HAPContextDefinitionLeafConstant refContextDefEle = (HAPContextDefinitionLeafConstant)HAPUtilityContext.getDescendant(contextGroup, refNodeId.getCategary(), refNodeId.getName());
					solidateConstantDefEle(refContextDefEle, contextGroup, contextProcessRequirement);
//					scriptConstants.put(scriptConstantName, refContextDefEle.getValue());
					expProcessContext.addConstant(scriptConstantName, refContextDefEle.getValue());
				}
				
				//process expression in scriptExpression
				HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(sciptExpressionDef, expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), contextProcessRequirement.expressionManager, contextProcessRequirement.runtime);
				
				//execute script expression
				HAPRuntimeTaskExecuteScriptExpression task = new HAPRuntimeTaskExecuteScriptExpression(scriptExpression, null, expProcessContext.getConstants());
				HAPServiceData serviceData = contextProcessRequirement.runtime.executeTaskSync(task);
				
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
	
	private static HAPContextDefinitionRootId solveReferencedNodeId(HAPContextDefinitionRootId nodeId, HAPContextGroup candidateGroup) {
		if(nodeId.getCategary()!=null)   return nodeId;
		for(String categary : HAPContextGroup.getVisibleContextTypes()) {
			HAPContextDefinitionElement refContextEle = HAPUtilityContext.getDescendant(candidateGroup, categary, nodeId.getName());
			if(refContextEle!=null && HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(refContextEle.getType())) {
				return new HAPContextDefinitionRootId(categary, nodeId.getName());
			}
		}
		return null;
	}
}
