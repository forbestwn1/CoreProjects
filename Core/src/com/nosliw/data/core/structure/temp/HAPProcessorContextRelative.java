package com.nosliw.data.core.structure.temp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPElementNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoPathToSolidRoot;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextRelative {

	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(context, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}

	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	//dependency: 
	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		contextGroup = process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		return contextGroup.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String parentName : allParentName(parent)) {
			HAPValueStructureDefinition context = HAPUtilityContext.getReferedStructure(parentName, parent, contextGroup);
			out = process(out, parentName, (HAPValueStructureDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(context, false), dependency, errors, context.isFlat(), configure, runtimeEnv);			
		}
		return out;
	}
	
	private static List<String> allParentName(HAPContainerStructure parent){
		List<String> out = new ArrayList<String>();
		out.addAll(parent.getStructureNames());
		out.add(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF);
		return out;
	}
	
	private static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, String parentName, HAPValueStructureDefinitionGroup parentContextGroup, Set<String> dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()){
			Map<String, HAPRoot> eles = out.getRootsByCategary(categary);
			for(String eleName : eles.keySet()) {
				HAPRoot contextRoot = eles.get(eleName);
				contextRoot.setDefinition(processRelativeInContextDefinitionElement(new HAPInfoElement(contextRoot.getDefinition(), new HAPReferenceElement(categary, eleName)), parentName, parentContextGroup, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
		}
		return out;
	}

	private static HAPElement processRelativeInContextDefinitionElement(HAPInfoElement contextEleInfo, String parentName, HAPValueStructureDefinitionGroup parentContext, Set<String>  dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPElement defContextElement = contextEleInfo.getElement();
		HAPElement out = defContextElement;
		switch(defContextElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeContextElement = (HAPElementLeafRelative)defContextElement;
			if(parentName.equals(relativeContextElement.getParent())) {
				if(dependency!=null)  dependency.add(parentName);
				if(!relativeContextElement.isProcessed()){
					List<String> categaryes = new ArrayList<String>();
					if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
					else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPValueStructureDefinitionGroup.getVisibleCategaries()));
					else   categaryes.addAll(Arrays.asList(configure.parentCategary));
					out = processRelativeContextDefinitionElement(contextEleInfo, parentContext, errors, isParentFlat, categaryes.toArray(new String[0]), configure, runtimeEnv);
				}
			}
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPElement> processedChildren = new LinkedHashMap<String, HAPElement>();
			HAPElementNode nodeContextElement = (HAPElementNode)defContextElement;
			for(String childName : nodeContextElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInContextDefinitionElement(new HAPInfoElement(nodeContextElement.getChild(childName), contextEleInfo.getElementPath().appendSegment(childName)), parentName, parentContext, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
			break;
		}
		return out;
	}
	
	private static HAPElement processRelativeContextDefinitionElement(HAPInfoElement contextEleInfo, HAPValueStructureDefinitionGroup parentContext, List<HAPServiceData> errors, boolean isParentFlat, String[] categaryes, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv){
		HAPElementLeafRelative defContextElementRelative = (HAPElementLeafRelative)contextEleInfo.getElement();
		HAPElement out = defContextElementRelative;
		
		HAPReferenceElement path = defContextElementRelative.getPathFormat(); 
		HAPInfoReferenceResolve resolveInfo = HAPUtilityContext.analyzeElementReference(path, parentContext, categaryes, configure.elementReferenceResolveMode);
		
		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(contextEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
			{
				//if refer to a constant element
				out = new HAPElementLeafConstant();
				Object constantValue = ((HAPElementLeafConstant)resolveInfo.referredRoot.getDefinition()).getValue();
				((HAPElementLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				if(isParentFlat)  path.getRootReference().setCategary(null);
				else path.getRootReference().setCategary(resolveInfo.path.getRootReference().getCategary());
				defContextElementRelative.setPath(path);
				
				HAPElement solvedContextEle = resolveInfo.resolvedElement; 
				if(solvedContextEle!=null){
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						String refPath = null;
						HAPElement parentContextEle = resolveInfo.referedRealElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = HAPNamingConversionUtility.cascadePath(parentSolidNodeRef.getPath(), resolveInfo.remainSolidPath);
						}
						else {
							refRootId = resolveInfo.referredRoot.getId();
							refPath = HAPNamingConversionUtility.cascadePath(resolveInfo.path.getSubPath(), resolveInfo.remainSolidPath);
						}
						defContextElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, refPath));
					}
					
					HAPElement relativeContextEle = defContextElementRelative.getDefinition();
					if(relativeContextEle==null) {
						defContextElementRelative.setDefinition(solvedContextEle.cloneStructureElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityContext.mergeElement(solvedContextEle, relativeContextEle, false, matchers, null, runtimeEnv);
						//remove all the void matchers
						Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
						for(String p : matchers.keySet()){
							HAPMatchers match = matchers.get(p);
							if(!match.isVoid()){
								noVoidMatchers.put(p, match);
							}
						}
						defContextElementRelative.setMatchers(noVoidMatchers);
						
						//inherit rule from parent
						if(configure.relativeInheritRule) {
							HAPElement solidParent = solvedContextEle.getSolidStructureElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPElementLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPElementLeafData)relativeContextEle).getDataInfo();
									relativeDataInfo.addRule(rule);
									relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
								}
							}
						}
					}
				}
				else{
					//not find parent node, throw exception
					errors.add(HAPServiceData.createFailureData(contextEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
				}
			}
			}			
		}

		return out;
	}

}
