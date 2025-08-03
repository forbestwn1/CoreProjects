package com.nosliw.core.application.division.story;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionScriptExpressionItemInContainer;
import com.nosliw.core.application.common.scriptexpression.HAPManualExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoRelativeResolve;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructureWithScriptExpression;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPValueStructureImp;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElement;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.division.story.brick.HAPStoryInfoVariable;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChangeWrapper;
import com.nosliw.core.xxx.application.valueport.HAPResultReferenceResolve;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup;

public class HAPStoryUtilityVariable {

	public static void buildUINodeVariable(HAPValueStructure valueStructure, Map<String, Object> constants, List<HAPStoryInfoVariable> variablesFromParent, HAPStoryRequestChangeWrapper changeRequest, HAPRuntimeEnvironment runtTimeEnv) {

		HAPStoryStory story = changeRequest.getStory(); 
		
		//resolve constants in valuestructure
		HAPDefinitionContainerScriptExpression containerEle = new HAPDefinitionContainerScriptExpression();
		HAPUtilityStructureWithScriptExpression.discoverConstantScript(valueStructure, containerEle);
	
		//resolve constants in value structure
		HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo = new HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup();
		for(HAPDefinitionScriptExpressionItemInContainer item :  containerEle.getItems()) {
			Map<String, HAPDefinitionConstant> constantsDef = brick.getConstantDefinitions();
			Map<String, Object> constants = new LinkedHashMap<String, Object>();
			for(String name : constantsDef.keySet()) {
				constants.put(name, constantsDef.get(name).getValue());
			}
			
			HAPManualExpressionScript scriptExpression = HAPUtilityScriptExpressionConstant.processScriptExpressionConstant((HAPDefinitionScriptExpression)item.getValue(), constantsDef, runtTimeEnv.getDataExpressionParser());
			taskInfo.addScriptExpressionInfo(item.getName(), scriptExpression, constants);
		}

		HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(taskInfo, runtTimeEnv);

		HAPServiceData serviceData = runtTimeEnv.getRuntime().executeTaskSync(task);
		JSONObject serviceDataJson = (JSONObject)serviceData.getData();
		Map<String, Object> scriptExpressionValues = new LinkedHashMap<String, Object>();
		for(Object key : serviceDataJson.keySet()) {
			scriptExpressionValues.put((String)key, scriptExpressionValues.get(key));
		}
		HAPUtilityStructureWithScriptExpression.solidateConstantScript(valueStructure, scriptExpressionValues);
		
		
		
		//build value structure from variables
		HAPValueStructure valueStructureFromParent = new HAPValueStructureImp();
		for(HAPStoryInfoVariable varInfo : variablesFromParent) {
			HAPStoryNodeVariable variableNode = (HAPStoryNodeVariable)story.getElement(varInfo.getVariableElementId());
			HAPVariableDataInfo varDataInfo = variableNode.getVariableInfo().getDataInfo();
			HAPElementStructureLeafData dataStructureEle = new HAPElementStructureLeafData(varDataInfo);
			HAPRootInStructure root = new HAPRootInStructure(dataStructureEle, varInfo);
		}
		
		//resolve relative
		Map<String, HAPRootInStructure> roots = valueStructure.getRoots();
		for(String rootName : roots.keySet()) {
			HAPUtilityStructure.traverseElement(roots.get(rootName).getDefinition(), null, new HAPProcessorStructureElement() {

				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					HAPElementStructure ele = eleInfo.getElement();
					String eleType = ele.getType();
					if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE)) {
						HAPElementStructureLeafRelativeForValue forValueEle = (HAPElementStructureLeafRelativeForValue)ele;
						HAPResultReferenceResolve resolveInfo = HAPUtilityProcessRelativeElement.analyzeElementReference(forValueEle.getReference().getElementPath(), valueStructureFromParent, null);
						resolveInfo.finalElement = HAPUtilityProcessRelativeElement.resolveFinalElement(resolveInfo.elementInfoSolid, false);
						forValueEle.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
					}
					else if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION)) {
						HAPElementStructureLeafRelativeForDefinition relativeEle = (HAPElementStructureLeafRelativeForDefinition)ele;
						HAPResultReferenceResolve resolveInfo = HAPUtilityProcessRelativeElement.analyzeElementReference(relativeEle.getReference().getElementPath(), valueStructureFromParent, null);
						resolveInfo.finalElement = HAPUtilityProcessRelativeElement.resolveFinalElement(resolveInfo.elementInfoSolid, false);
						relativeEle.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
					}
					
					
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {
				}
				
			}, null);
		}
		
		
	}
	
	
	public static List<HAPStoryNodeVariable> getVariableForChild(HAPEntityOrReference entityOrRef, HAPStoryRequestChangeWrapper changeRequest){
		HAPStoryNode storyNode = (HAPStoryNode)HAPStoryUtilityStory.getStoryElement(entityOrRef, changeRequest.getStory());
		return HAPStoryUtilityStory.getAllChildNodeByType(storyNode, HAPStoryNodeVariable.TYPE, changeRequest.getStory()).stream().filter(childInfo->isVariableForChild(childInfo)).map(childInfo->(HAPStoryNodeVariable)childInfo.getChildNode()).collect(Collectors.toList());
	}

	public static void addVariableToNode(HAPStoryReferenceElement nodeRef, HAPStoryReferenceElement variableRef, HAPInfo setting, HAPStoryRequestChangeWrapper changeRequest) {
		HAPStoryNodeVariable variableNode =  (HAPStoryNodeVariable)changeRequest.getStory().getElement(variableRef);
		changeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(nodeRef, variableRef, variableNode.getName(), setting));
	}
	
	private static boolean isVariableForChild(HAPStoryInfoNodeChild varChildInfo) {
		return true;
	}

	
	
}
