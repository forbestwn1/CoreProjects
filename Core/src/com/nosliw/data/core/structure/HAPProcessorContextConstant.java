package com.nosliw.data.core.structure;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;
import com.nosliw.data.core.value.HAPResourceDefinitionValue;

public class HAPProcessorContextConstant {

	static public HAPStructureValueDefinitionGroup process(
			HAPStructureValueDefinitionGroup originalContextGroup,
			HAPParentContext parent,
			HAPContainerAttachment attachmentContainer,
			String inheritMode,
			HAPRuntimeEnvironment runtimeEnv){

		//merge with parent
		HAPStructureValueDefinitionGroup merged = originalContextGroup;
		for(String parentName : parent.getNames()) {
			merged = mergeWithParent(merged, (HAPStructureValueDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedContext(parentName, parent, merged), false), inheritMode);
		}

		//process constant ref in context
		HAPStructureValueDefinitionGroup out =  solidateConstantRefs(merged, attachmentContainer, runtimeEnv);

		//figure out constant value (some constant may use another constant)
		out =  solidateConstantDefs(out, runtimeEnv);
		
		//figure out root that ture out to be constant value, then convert to constant root
		out = discoverConstantContextRoot(out);
		
		return out;
	}

	
	//merge constant with parent
	//child constant has higher priority than parent
	private static HAPStructureValueDefinitionGroup mergeWithParent(
			HAPStructureValueDefinitionGroup contextGroup,
			HAPStructureValueDefinitionGroup parentContextGroup,
			String inheritMode){
		HAPStructureValueDefinitionGroup out = contextGroup.cloneContextGroup();
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			if(parentContextGroup!=null) {
				//merge constants with parent
				for(String contextCategary : HAPStructureValueDefinitionGroup.getInheritableContextTypes()) {
					for(String name : parentContextGroup.getContext(contextCategary).getRootNames()) {
						if(parentContextGroup.getElement(contextCategary, name).isConstant()) {
							if(contextGroup.getElement(contextCategary, name)==null) {
								out.addElement(name, parentContextGroup.getElement(contextCategary, name).cloneRoot(), contextCategary);
							}
						}
					}
				}
			} 
		}
		return out;
	}

	//find all the context root which is actually constant, convert it to constant element 
	static private HAPStructureValueDefinitionGroup discoverConstantContextRoot(HAPStructureValueDefinitionGroup contextGroup) {
		for(String contextType : contextGroup.getContextTypes()) {
			HAPStructureValueDefinitionFlat context = contextGroup.getContext(contextType);
			for(String eleName : context.getRootNames()) {
				HAPRoot contextRoot = context.getRoot(eleName);
				HAPElement ele = contextRoot.getDefinition();
				if (HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(ele.getType())) {
					Object value = discoverConstantValue(ele);
					if(value!=null) {
						HAPElementLeafConstant constantEle = new HAPElementLeafConstant();
						constantEle.setValue(value);
						contextRoot.setDefinition(constantEle);
					}
				}
			}
		}
		return contextGroup;
	}
	
	static private Object discoverConstantValue(HAPElement contextDefEle) {
		String type = contextDefEle.getType();
		if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(type)) {
			HAPElementLeafConstant constantEle = (HAPElementLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		else if (HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(type)) {
			HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
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

	static private HAPStructureValueDefinitionGroup solidateConstantRefs(
			HAPStructureValueDefinitionGroup contextGroup,
			HAPContainerAttachment attachmentContainer,
			HAPRuntimeEnvironment runtimeEnv){
		if(attachmentContainer==null)   return contextGroup;
		HAPStructureValueDefinitionGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPStructureValueDefinitionGroup.getAllContextTypes()) {
			Map<String, HAPRoot> cotextDefRoots = out.getElements(categary);
			for(String name : cotextDefRoots.keySet()) {
				HAPRoot contextDefRoot = cotextDefRoots.get(name);
				HAPUtilityContext.processContextRootElement(contextDefRoot, name, new HAPProcessorContextDefinitionElement() {
					@Override
					public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF.equals(eleInfo.getElement().getType())) {
							HAPElementLeafConstantReference constantRefEle = (HAPElementLeafConstantReference)eleInfo.getElement();
							HAPResourceDefinitionValue valueResourceDef = (HAPResourceDefinitionValue)HAPUtilityAttachment.getResourceDefinition(attachmentContainer, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, constantRefEle.getConstantName(), runtimeEnv.getResourceDefinitionManager());
							HAPElementLeafConstant newEle = new HAPElementLeafConstant(valueResourceDef.getValue().getValue());
							return Pair.of(true, newEle);
						}
						return null;
					}

					@Override
					public void postProcess(HAPInfoElement eleInfo, Object value) {}
				}, null);
			}
		}
		return out;
	}


	
	/**
	 * process all constants defs to get data of constant
	 * it will keep the json structure and only calculate the leaf value 
	 * 		constantDefs : all available constants
	 */
	static private HAPStructureValueDefinitionGroup solidateConstantDefs(
			HAPStructureValueDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv){
		HAPStructureValueDefinitionGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPStructureValueDefinitionGroup.getAllContextTypes()) {
			Map<String, HAPRoot> cotextDefRoots = out.getElements(categary);
			for(String name : cotextDefRoots.keySet()) {
				HAPRoot contextDefRoot = cotextDefRoots.get(name);
				HAPUtilityContext.processContextRootElement(contextDefRoot, name, new HAPProcessorContextDefinitionElement() {
					@Override
					public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(eleInfo.getElement().getType())) {
							solidateConstantDefEle((HAPElementLeafConstant)eleInfo.getElement(), contextGroup, runtimeEnv);
						}
						return null;
					}

					@Override
					public void postProcess(HAPInfoElement eleInfo, Object value) {}
				}, null);
			}
		}
		return out;
	}

	static private void solidateConstantDefEle(
			HAPElementLeafConstant contextDefConstant, 
			HAPStructureValueDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv) {

		if(!contextDefConstant.isProcessed()) {
			Object data = processConstantDefJsonNode(contextDefConstant.getValue(), contextGroup, runtimeEnv);
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
			HAPStructureValueDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv) {
		Object out = null;
		try{
			if(nodeValue instanceof JSONObject){
				JSONObject outJsonObj = new JSONObject();
				JSONObject jsonObj = (JSONObject)nodeValue;
				Iterator<String> keys = jsonObj.keys();
				while(keys.hasNext()){
					String key = keys.next();
					Object childValue = jsonObj.get(key);
					Object data = processConstantDefJsonNode(childValue, contextGroup, runtimeEnv);
					if(data!=null)  outJsonObj.put(key, data);   
				}
				out = outJsonObj;
			}
			else if(nodeValue instanceof JSONArray){
				JSONArray outJsonArray = new JSONArray();
				JSONArray jsonArray = (JSONArray)nodeValue;
				for(int i=0; i<jsonArray.length(); i++){
					Object childNode = jsonArray.get(i);
					Object data = processConstantDefJsonNode(childNode, contextGroup, runtimeEnv);
					if(data!=null)   outJsonArray.put(i, data);
				}
				out = outJsonArray;
			}
			else{
				out = processConstantDefLeaf(nodeValue, contextGroup, runtimeEnv);
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
			HAPStructureValueDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv) {

		//simply process script
		HAPExecutableScriptGroup groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, null, runtimeEnv.getExpressionManager(), HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), runtimeEnv, new HAPProcessTracker());
		HAPExecutableScriptEntity scriptExe = groupExe.getScript(null);
		
		String scriptType = scriptExe.getScriptType();
		//if pure data
		if(HAPConstantShared.SCRIPT_TYPE_TEXT.equals(scriptType))  return leafData;
		
		///if contain script
		//discover all constant
		Map<String, Object> constantsValue = new LinkedHashMap<String, Object>();
		Set<HAPDefinitionConstant> constantsDef = groupExe.getConstantsDefinition();
		for(HAPDefinitionConstant constantDef : constantsDef){
			String constantId = constantDef.getId();
			HAPIdContextDefinitionRoot refNodeId = solveReferencedNodeId(new HAPIdContextDefinitionRoot(constantId), contextGroup);
			HAPElementLeafConstant refContextDefEle = (HAPElementLeafConstant)HAPUtilityContext.getDescendant(contextGroup, refNodeId.getCategary(), refNodeId.getName());
			solidateConstantDefEle(refContextDefEle, contextGroup, runtimeEnv);
			constantsValue.put(constantId, refContextDefEle.getValue());
		}

		//process script again with constant and discovery
		groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, constantsValue, runtimeEnv.getExpressionManager(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());		

		//execute script expression
		HAPRuntimeTaskExecuteScript task = new HAPRuntimeTaskExecuteScript(groupExe, null, null, null);
		HAPServiceData out = runtimeEnv.getRuntime().executeTaskSync(task);
		return out.getData();
	}
	
	private static HAPIdContextDefinitionRoot solveReferencedNodeId(HAPIdContextDefinitionRoot nodeId, HAPStructureValueDefinitionGroup candidateGroup) {
		if(nodeId.getCategary()!=null)   return nodeId;
		for(String categary : HAPStructureValueDefinitionGroup.getVisibleContextTypes()) {
			HAPElement refContextEle = HAPUtilityContext.getDescendant(candidateGroup, categary, nodeId.getName());
			if(refContextEle!=null && HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(refContextEle.getType())) {
				return new HAPIdContextDefinitionRoot(categary, nodeId.getName());
			}
		}
		return null;
	}
}
