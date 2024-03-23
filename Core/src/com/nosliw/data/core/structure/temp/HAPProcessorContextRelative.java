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
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafConstant;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoPathToSolidRoot;
import com.nosliw.core.application.common.structure.HAPReferenceElementInStructure;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextRelative {

	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(context, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}

	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	//dependency: 
	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		contextGroup = process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		return contextGroup.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String parentName : allParentName(parent)) {
			HAPValueStructureInValuePort context = HAPUtilityContext.getReferedStructure(parentName, parent, contextGroup);
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
	
	private static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, String parentName, HAPValueStructureDefinitionGroup parentContextGroup, Set<String> dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = contextGroup.cloneValueStructureGroup();
		for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()){
			Map<String, HAPRootStructure> eles = out.getRootsByCategary(categary);
			for(String eleName : eles.keySet()) {
				HAPRootStructure contextRoot = eles.get(eleName);
				contextRoot.setDefinition(processRelativeInContextDefinitionElement(new HAPInfoElement(contextRoot.getDefinition(), new HAPReferenceElementInStructure(categary, eleName)), parentName, parentContextGroup, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
		}
		return out;
	}

	private static HAPElementStructure processRelativeInContextDefinitionElement(HAPInfoElement contextEleInfo, String parentName, HAPValueStructureDefinitionGroup parentContext, Set<String>  dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructure defContextElement = contextEleInfo.getElement();
		HAPElementStructure out = defContextElement;
		switch(defContextElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementStructureLeafRelative relativeContextElement = (HAPElementStructureLeafRelative)defContextElement;
			if(parentName.equals(relativeContextElement.getParentValueContextName())) {
				if(dependency!=null)  dependency.add(parentName);
				if(!relativeContextElement.isProcessed()){
					List<String> categaryes = new ArrayList<String>();
					if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
					else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPValueStructureDefinitionGroup.getVisibleToChildCategaries()));
					else   categaryes.addAll(Arrays.asList(configure.parentCategary));
					out = processRelativeContextDefinitionElement(contextEleInfo, parentContext, errors, isParentFlat, categaryes.toArray(new String[0]), configure, runtimeEnv);
				}
			}
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeContextElement = (HAPElementStructureNode)defContextElement;
			for(String childName : nodeContextElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInContextDefinitionElement(new HAPInfoElement(nodeContextElement.getChild(childName), contextEleInfo.getElementPath().appendSegment(childName)), parentName, parentContext, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
			break;
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeContextDefinitionElement(HAPInfoElement contextEleInfo, HAPValueStructureDefinitionGroup parentContext, List<HAPServiceData> errors, boolean isParentFlat, String[] categaryes, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructureLeafRelative defContextElementRelative = (HAPElementStructureLeafRelative)contextEleInfo.getElement();
		HAPElementStructure out = defContextElementRelative;
		
		HAPReferenceElementInStructure path = defContextElementRelative.getPathFormat(); 
		HAPResultReferenceResolve resolveInfo = HAPUtilityContext.analyzeElementReference(path, parentContext, categaryes, configure.elementReferenceResolveMode);
		
		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(contextEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getBrickTypeId()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
			{
				//if refer to a constant element
				out = new HAPElementStructureLeafConstant();
				Object constantValue = ((HAPElementStructureLeafConstant)resolveInfo.referredRoot.getDefinition()).getValue();
				((HAPElementStructureLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				if(isParentFlat)  path.getRootReference().setCategary(null);
				else path.getRootReference().setCategary(resolveInfo.path.getRootReference().getCategary());
				defContextElementRelative.setReference(path);
				
				HAPElementStructure solvedContextEle = resolveInfo.finalElement; 
				if(solvedContextEle!=null){
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						String refPath = null;
						HAPElementStructure parentContextEle = resolveInfo.referedRealElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = HAPUtilityNamingConversion.cascadePath(parentSolidNodeRef.getPath(), resolveInfo.remainSolidPath);
						}
						else {
							refRootId = resolveInfo.referredRoot.getId();
							refPath = HAPUtilityNamingConversion.cascadePath(resolveInfo.path.getSubPath(), resolveInfo.remainSolidPath);
						}
						defContextElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, refPath));
					}
					
					HAPElementStructure relativeContextEle = defContextElementRelative.getDefinition();
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
							HAPElementStructure solidParent = solvedContextEle.getSolidStructureElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeContextEle).getDataInfo();
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
