package com.nosliw.data.core.structure.temp;

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
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstantReference;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPReferenceRootInStrucutre;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionGroup;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.HAPProcessorScript;
import com.nosliw.data.core.value.HAPResourceDefinitionValue;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextConstant {

	static public HAPValueStructureDefinitionGroup process(
			HAPValueStructureDefinitionGroup originalContextGroup,
			HAPContainerStructure parent,
			HAPDefinitionEntityContainerAttachment attachmentContainer,
			String inheritMode,
			HAPRuntimeEnvironment runtimeEnv){

		//merge with parent
		HAPValueStructureDefinitionGroup merged = originalContextGroup;
		for(String parentName : parent.getStructureNames()) {
			merged = (HAPValueStructureDefinitionGroup)mergeWithParent(merged, HAPUtilityContextStructure.toSolidContextStructure((HAPValueStructureInValuePort)HAPUtilityStructure.getReferedStructure(parentName, parent, merged), false), inheritMode);
		}

		//process constant ref in context
		HAPValueStructureDefinitionGroup out =  solidateConstantRefs(merged, attachmentContainer, runtimeEnv);

		//figure out constant value (some constant may use another constant)
		out =  solidateConstantDefs(out, runtimeEnv);
		
		//figure out root that ture out to be constant value, then convert to constant root
		out = discoverConstantContextRoot(out);
		
		return out;
	}

	
	//merge constant with parent
	//child constant has higher priority than parent
	private static HAPValueStructureInValuePort mergeWithParent(
			HAPValueStructureInValuePort valueStructure,
			HAPValueStructureInValuePort parentValueStructure,
			String inheritMode){
		HAPValueStructureInValuePort out = (HAPValueStructureInValuePort)valueStructure.cloneStructure();
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			if(parentValueStructure!=null) {
				//merge constants with parent
				for(HAPRootStructure root : parentValueStructure.getAllRoots()) {
					if(root.isConstant()) {
						HAPReferenceRootInStrucutre rootReference = parentValueStructure.getRootReferenceById(root.getLocalId());
						if(out.resolveRoot(rootReference, false)==null) {
							out.addRoot(rootReference, root);
						}
					}
				}
			}
		}
		return out;
	}

	//find all the context root which is actually constant, convert it to constant element 
	static private HAPValueStructureDefinitionGroup discoverConstantContextRoot(HAPValueStructureDefinitionGroup contextGroup) {
		for(String contextType : contextGroup.getCategaries()) {
			HAPValueStructureDefinitionFlat context = contextGroup.getFlat(contextType);
			for(String eleName : context.getRootNames()) {
				HAPRootStructure contextRoot = context.getRoot(eleName);
				HAPElementStructure ele = contextRoot.getDefinition();
				if (HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(ele.getType())) {
					Object value = discoverConstantValue(ele);
					if(value!=null) {
						HAPElementStructureLeafConstant constantEle = new HAPElementStructureLeafConstant();
						constantEle.setValue(value);
						contextRoot.setDefinition(constantEle);
					}
				}
			}
		}
		return contextGroup;
	}
	
	static private Object discoverConstantValue(HAPElementStructure contextDefEle) {
		String type = contextDefEle.getType();
		if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(type)) {
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		else if (HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(type)) {
			HAPElementStructureNode nodeEle = (HAPElementStructureNode)contextDefEle;
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

	static private HAPValueStructureDefinitionGroup solidateConstantRefs(
			HAPValueStructureDefinitionGroup contextGroup,
			HAPDefinitionEntityContainerAttachment attachmentContainer,
			HAPRuntimeEnvironment runtimeEnv){
		if(attachmentContainer==null)   return contextGroup;
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()) {
			Map<String, HAPRootStructure> cotextDefRoots = out.getRootsByCategary(categary);
			for(String name : cotextDefRoots.keySet()) {
				HAPRootStructure contextDefRoot = cotextDefRoots.get(name);
				HAPUtilityContext.processContextRootElement(contextDefRoot, name, new HAPProcessorStructureElement() {
					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF.equals(eleInfo.getElement().getType())) {
							HAPElementStructureLeafConstantReference constantRefEle = (HAPElementStructureLeafConstantReference)eleInfo.getElement();
							HAPResourceDefinitionValue valueResourceDef = (HAPResourceDefinitionValue)HAPUtilityAttachment.getResourceDefinition(attachmentContainer, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, constantRefEle.getConstantName(), runtimeEnv.getResourceDefinitionManager());
							HAPElementStructureLeafConstant newEle = new HAPElementStructureLeafConstant(valueResourceDef.getValue().getValue());
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
	static private HAPValueStructureDefinitionGroup solidateConstantDefs(
			HAPValueStructureDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv){
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()) {
			Map<String, HAPRootStructure> cotextDefRoots = out.getRootsByCategary(categary);
			for(String name : cotextDefRoots.keySet()) {
				HAPRootStructure contextDefRoot = cotextDefRoots.get(name);
				HAPUtilityContext.processContextRootElement(contextDefRoot, name, new HAPProcessorStructureElement() {
					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(eleInfo.getElement().getType())) {
							solidateConstantDefEle((HAPElementStructureLeafConstant)eleInfo.getElement(), contextGroup, runtimeEnv);
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
			HAPElementStructureLeafConstant contextDefConstant, 
			HAPValueStructureDefinitionGroup contextGroup,
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
			HAPValueStructureDefinitionGroup contextGroup,
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
			HAPValueStructureDefinitionGroup contextGroup,
			HAPRuntimeEnvironment runtimeEnv) {

		//simply process script
		HAPExecutableScriptGroup groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, null, runtimeEnv.getExpressionManager(), HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), runtimeEnv, new HAPProcessTracker());
		HAPExecutableScriptEntity scriptExe = groupExe.getScript(null);
		
		String scriptType = scriptExe.getScriptType();
		//if pure data
		if(HAPConstantShared.EXPRESSION_TYPE_TEXT.equals(scriptType))  return leafData;
		
		///if contain script
		//discover all constant
		Map<String, Object> constantsValue = new LinkedHashMap<String, Object>();
		Set<HAPDefinitionConstant> constantsDef = groupExe.getConstantsDefinition();
		for(HAPDefinitionConstant constantDef : constantsDef){
//			String constantId = constantDef.getId();
//			HAPIdContextDefinitionRoot refNodeId = solveReferencedNodeId(new HAPIdContextDefinitionRoot(constantId), contextGroup);
//			HAPElementLeafConstant refContextDefEle = (HAPElementLeafConstant)HAPUtilityContext.getDescendant(contextGroup, refNodeId.getCategary(), refNodeId.getName());
//			solidateConstantDefEle(refContextDefEle, contextGroup, runtimeEnv);
//			constantsValue.put(constantId, refContextDefEle.getValue());
		}

		//process script again with constant and discovery
		groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, constantsValue, runtimeEnv.getExpressionManager(), HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());		

		//execute script expression
		HAPRuntimeTaskExecuteRhinoScriptExpressionGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionGroup(groupExe, null, null, null);
		HAPServiceData out = runtimeEnv.getRuntime().executeTaskSync(task);
		return out.getData();
	}
	
//	private static HAPIdContextDefinitionRoot solveReferencedNodeId(HAPIdContextDefinitionRoot nodeId, HAPValueStructureDefinitionGroup candidateGroup) {
//		if(nodeId.getCategary()!=null)   return nodeId;
//		for(String categary : HAPValueStructureDefinitionGroup.getVisibleCategaries()) {
//			HAPElement refContextEle = HAPUtilityContext.getDescendant(candidateGroup, categary, nodeId.getName());
//			if(refContextEle!=null && HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(refContextEle.getType())) {
////				return new HAPIdContextDefinitionRoot(categary, nodeId.getName());
//			}
//		}
//		return null;
//	}
}
