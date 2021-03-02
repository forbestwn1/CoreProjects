package com.nosliw.data.core.script.context;

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

public class HAPProcessorContextRelative {

	public static HAPContext process(HAPContext context, HAPParentContext parent, List<HAPServiceData> errors, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(context, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}

	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPParentContext parent, List<HAPServiceData> errors, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	//dependency: 
	public static HAPContext process(HAPContext context, HAPParentContext parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextGroup contextGroup = new HAPContextGroup();
		contextGroup.setContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		contextGroup = process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		return contextGroup.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPParentContext parent, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String parentName : allParentName(parent)) {
			HAPContextStructure context = HAPUtilityContext.getReferedContext(parentName, parent, contextGroup);
			out = process(out, parentName, (HAPContextGroup)HAPUtilityContextStructure.toSolidContextStructure(context, false), dependency, errors, context.isFlat(), configure, runtimeEnv);			
		}
		return out;
	}
	
	private static List<String> allParentName(HAPParentContext parent){
		List<String> out = new ArrayList<String>();
		out.addAll(parent.getNames());
		out.add(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF);
		return out;
	}
	
	private static HAPContextGroup process(HAPContextGroup contextGroup, String parentName, HAPContextGroup parentContextGroup, Set<String> dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String categary : HAPContextGroup.getAllContextTypes()){
			Map<String, HAPContextDefinitionRoot> eles = out.getElements(categary);
			for(String eleName : eles.keySet()) {
				HAPContextDefinitionRoot contextRoot = eles.get(eleName);
				contextRoot.setDefinition(processRelativeInContextDefinitionElement(new HAPInfoContextNode(contextRoot.getDefinition(), new HAPContextPath(categary, eleName)), parentName, parentContextGroup, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
		}
		return out;
	}

	private static HAPContextDefinitionElement processRelativeInContextDefinitionElement(HAPInfoContextNode contextEleInfo, String parentName, HAPContextGroup parentContext, Set<String>  dependency, List<HAPServiceData> errors, boolean isParentFlat, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextDefinitionElement defContextElement = contextEleInfo.getContextElement();
		HAPContextDefinitionElement out = defContextElement;
		switch(defContextElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeContextElement = (HAPContextDefinitionLeafRelative)defContextElement;
			if(parentName.equals(relativeContextElement.getParent())) {
				dependency.add(parentName);
				if(!relativeContextElement.isProcessed()){
					List<String> categaryes = new ArrayList<String>();
					if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
					else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
					else   categaryes.addAll(Arrays.asList(configure.parentCategary));
					out = processRelativeContextDefinitionElement(contextEleInfo, parentContext, errors, isParentFlat, categaryes.toArray(new String[0]), configure, runtimeEnv);
				}
			}
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPContextDefinitionElement> processedChildren = new LinkedHashMap<String, HAPContextDefinitionElement>();
			HAPContextDefinitionNode nodeContextElement = (HAPContextDefinitionNode)defContextElement;
			for(String childName : nodeContextElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInContextDefinitionElement(new HAPInfoContextNode(nodeContextElement.getChild(childName), contextEleInfo.getContextPath().appendSegment(childName)), parentName, parentContext, dependency, errors, isParentFlat, configure, runtimeEnv));
			}
			break;
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeContextDefinitionElement(HAPInfoContextNode contextEleInfo, HAPContextGroup parentContext, List<HAPServiceData> errors, boolean isParentFlat, String[] categaryes, HAPConfigureContextProcessor configure, HAPRuntimeEnvironment runtimeEnv){
		HAPContextDefinitionLeafRelative defContextElementRelative = (HAPContextDefinitionLeafRelative)contextEleInfo.getContextElement();
		HAPContextDefinitionElement out = defContextElementRelative;
		
		HAPContextPath path = defContextElementRelative.getPath(); 
		HAPInfoContextElementReferenceResolve resolveInfo = HAPUtilityContext.resolveReferencedContextElement(path, parentContext, categaryes, configure.relativeResolveMode);
		
		if(resolveInfo==null || resolveInfo.rootNode==null) {
			errors.add(HAPServiceData.createFailureData(contextEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.rootNode.getDefinition().getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
			{
				//if refer to a constant element
				out = new HAPContextDefinitionLeafConstant();
				Object constantValue = ((HAPContextDefinitionLeafConstant)resolveInfo.rootNode.getDefinition()).getValue();
				((HAPContextDefinitionLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				if(isParentFlat)  path.getRootElementId().setCategary(null);
				else path.getRootElementId().setCategary(resolveInfo.path.getRootElementId().getCategary());
				defContextElementRelative.setPath(path);
				
				HAPContextDefinitionElement solvedContextEle = resolveInfo.resolvedNode; 
				if(solvedContextEle!=null){
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						String refPath = null;
						HAPContextDefinitionElement parentContextEle = resolveInfo.referedNode; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPReferenceContextNode parentSolidNodeRef = ((HAPContextDefinitionLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = HAPNamingConversionUtility.cascadePath(parentSolidNodeRef.getPath(), resolveInfo.remainPath);
						}
						else {
							refRootId = resolveInfo.rootNode.getId();
							refPath = HAPNamingConversionUtility.cascadePath(resolveInfo.path.getSubPath(), resolveInfo.remainPath);
						}
						defContextElementRelative.setSolidNodeReference(new HAPReferenceContextNode(refRootId, refPath));
					}
					
					HAPContextDefinitionElement relativeContextEle = defContextElementRelative.getDefinition();
					if(relativeContextEle==null) {
						defContextElementRelative.setDefinition(solvedContextEle.cloneContextDefinitionElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityContext.mergeContextDefitionElement(solvedContextEle, relativeContextEle, false, matchers, null, runtimeEnv);
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
							HAPContextDefinitionElement solidParent = solvedContextEle.getSolidContextDefinitionElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPContextDefinitionLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPContextDefinitionLeafData)relativeContextEle).getDataInfo();
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
