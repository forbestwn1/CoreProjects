package com.nosliw.data.core.structure;

import java.util.HashSet;
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
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.task.HAPInfoRuntimeTaskScript;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScript;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression.HAPProcessorScript;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.value.HAPResourceDefinitionValue;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;

public class HAPProcessorElementConstant {

	static public HAPStructure process(
			HAPStructure structure,
			HAPDefinitionEntityContainerAttachment attachmentContainer,
			HAPConfigureProcessorValueStructure configure,
			HAPRuntimeEnvironment runtimeEnv){

		//process constant ref in context
		HAPStructure out =  solidateConstantRefs(structure, attachmentContainer, runtimeEnv);

		//figure out constant value (some constant may use another constant)
		out =  solidateConstantElements(out, configure, runtimeEnv);
		
		//figure out root that ture out to be constant value, then convert to constant root
		out = discoverConstantContextRoot(out);
		
		return out;
	}

	static public HAPStructure process(
			HAPStructure structure,
			HAPContainerStructure parent,
			HAPDefinitionEntityContainerAttachment attachmentContainer,
			HAPConfigureProcessorValueStructure configure,
			HAPRuntimeEnvironment runtimeEnv){

		//merge with parent
		HAPStructure merged = structure;
		for(String parentName : parent.getStructureNames()) {
			merged = mergeWithParent(merged, HAPUtilityStructureElementReference.getReferedStructure(parentName, parent, merged), configure.inheritMode);
		}

		//process constant ref in context
		HAPStructure out =  solidateConstantRefs(structure, attachmentContainer, runtimeEnv);

		//figure out constant value (some constant may use another constant)
		out =  solidateConstantElements(out, configure, runtimeEnv);
		
		//figure out root that ture out to be constant value, then convert to constant root
		out = discoverConstantContextRoot(out);
		
		return out;
	}

	
	//merge constant with parent
	//child constant has higher priority than parent
	private static HAPStructure mergeWithParent(
			HAPStructure valueStructure,
			HAPStructure parentValueStructure,
			String inheritMode){
		HAPStructure out = valueStructure.cloneStructure();
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
	static private HAPStructure discoverConstantContextRoot(HAPStructure structure) {
		HAPStructure out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			Object value = discoverConstantValue(root.getDefinition());
			if(value!=null) {
				HAPElementStructureLeafConstant constantEle = new HAPElementStructureLeafConstant(value);
				root.setDefinition(constantEle);
			}
		}
		return out;
	}
	
	static private Object discoverConstantValue(HAPElementStructure element) {
		String type = element.getType();
		if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(type)) {
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)element;
			return constantEle.getValue();
		}
		else if (HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(type)) {
			HAPElementStructureNode nodeEle = (HAPElementStructureNode)element;
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

	//process constant reference element, replace with constant element
	static private HAPStructure solidateConstantRefs(
			HAPStructure structure,
			HAPDefinitionEntityContainerAttachment attachmentContainer,
			HAPRuntimeEnvironment runtimeEnv){
		if(attachmentContainer==null)   return structure;
		HAPStructure out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANTREF.equals(eleInfo.getElement().getType())) {
						HAPElementStructureLeafConstantReference constantRefEle = (HAPElementStructureLeafConstantReference)eleInfo.getElement();
						HAPResourceDefinitionValue valueResourceDef = (HAPResourceDefinitionValue)HAPUtilityAttachment.getResourceDefinition(attachmentContainer, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, constantRefEle.getConstantName(), runtimeEnv.getResourceDefinitionManager());
						HAPElementStructureLeafConstant newEle = new HAPElementStructureLeafConstant(valueResourceDef.getValue().getValue());
						return Pair.of(false, newEle);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {}
			}, null);
		}
		return out;
	}

	static private HAPStructure solidateConstantElements(
			HAPStructure structure,
			HAPConfigureProcessorValueStructure configure,
			HAPRuntimeEnvironment runtimeEnv){
		HAPStructure out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(eleInfo.getElement().getType())) {
						HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)eleInfo.getElement();
						solidateConstantDefEle(constantEle, structure, configure, runtimeEnv);
						return null;
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {}
			}, null);
		}
		return out;
	}

	static private void solidateConstantDefEle(
			HAPElementStructureLeafConstant contextElement, 
			HAPStructure structure,
			HAPConfigureProcessorValueStructure configure,
			HAPRuntimeEnvironment runtimeEnv) {

		if(!contextElement.isProcessed()) {
			Object data = processConstantDefJsonNode(contextElement.getValue(), structure, configure, runtimeEnv);
			if(data==null)   data = contextElement.getValue();
			contextElement.setValue(data);
			contextElement.processed();
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
			HAPStructure structure,
			HAPConfigureProcessorValueStructure configure,
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
					Object data = processConstantDefJsonNode(childValue, structure, configure, runtimeEnv);
					if(data!=null)  outJsonObj.put(key, data);   
				}
				out = outJsonObj;
			}
			else if(nodeValue instanceof JSONArray){
				JSONArray outJsonArray = new JSONArray();
				JSONArray jsonArray = (JSONArray)nodeValue;
				for(int i=0; i<jsonArray.length(); i++){
					Object childNode = jsonArray.get(i);
					Object data = processConstantDefJsonNode(childNode, structure, configure, runtimeEnv);
					if(data!=null)   outJsonArray.put(i, data);
				}
				out = outJsonArray;
			}
			else{
				out = processConstantDefLeaf(nodeValue, structure, configure, runtimeEnv);
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
			HAPStructure structure,
			HAPConfigureProcessorValueStructure configure,
			HAPRuntimeEnvironment runtimeEnv) {

		//simply process script
		HAPExecutableScriptGroup groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, null, HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), runtimeEnv, new HAPProcessTracker());
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
			Set<String> types = new HashSet<String>();
			types.add(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT);
			HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.analyzeElementReference(constantId, structure, configure.elementReferenceResolveMode, types);
			solidateConstantDefEle((HAPElementStructureLeafConstant)resolveInfo.realSolved.finalElement, structure, configure, runtimeEnv);
			constantsValue.put(constantId, ((HAPElementStructureLeafConstant)HAPUtilityStructureElementReference.resolveFinalElement(resolveInfo.realSolved, configure.relativeInheritRule)).getValue());
		}

		//process script again with constant and discovery
		groupExe = HAPProcessorScript.processSimpleScript(leafData.toString(), null, null, constantsValue, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());		

		//execute script expression
		HAPRuntimeTaskExecuteScript task = new HAPRuntimeTaskExecuteScript(new HAPInfoRuntimeTaskScript(groupExe, null, null, null), runtimeEnv);
		HAPServiceData out = runtimeEnv.getRuntime().executeTaskSync(task);
		return out.getData();
	}
	
}
