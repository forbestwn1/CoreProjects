package com.nosliw.data.core.structure.reference;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoPathToSolidRoot;
import com.nosliw.data.core.structure.HAPProcessorStructureElement;
import com.nosliw.data.core.structure.HAPStructure1;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;

public class HAPProcessorElementRelative {

	public static HAPStructure1 process(HAPStructure1 structure, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(structure, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}

	public static HAPStructure1 process(HAPStructure1 structure, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructure1 out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorStructureElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
						String parent = relativeEle.getReference().getParentValueContextName();
						if(dependency!=null)   dependency.add(parent);
						if(!relativeEle.isProcessed()) {
							HAPStructure1 parentStructure = HAPUtilityStructureElementReference.getReferedStructure(parent, parents, structure);
							return Pair.of(false, processRelativeElement(eleInfo, parentStructure, errors, configure, runtimeEnv));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}
			}, null);
		}
		return out;
	}
	
	public static HAPRootStructure process(HAPRootStructure root, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(root, root.getLocalId(), parents, dependency, errors, configure, runtimeEnv);		
	}
	
	public static HAPRootStructure process(HAPRootStructure root, String rootId, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPRootStructure out = root.cloneRoot();
		HAPUtilityStructure.traverseElement(out, rootId, new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					String parent = relativeEle.getReference().getParentValueContextName();
					if(dependency!=null)   dependency.add(parent);
					if(!relativeEle.isProcessed()) {
						HAPStructure1 parentStructure = HAPUtilityStructureElementReference.getReferedStructure(parent, parents, null);
						return Pair.of(false, processRelativeElement(eleInfo, parentStructure, errors, configure, runtimeEnv));
					}
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		return out;
	}
	
	private static HAPElementStructure processRelativeElement(HAPInfoElement eleInfo, HAPStructure1 parentStructure, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureLeafRelative relativeElement = (HAPElementStructureLeafRelative)eleInfo.getElement();
		HAPElementStructure out = relativeElement;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(relativeElement.getReference().getPath(), parentStructure, configure.elementReferenceResolveMode, configure.relativeInheritRule, null);
		
		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(eleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getEntityType()) {
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
				HAPElementStructure solvedElement = resolveInfo.finalElement; 
				if(solvedElement!=null){
					relativeElement.setResolvedIdPath(resolveInfo.path);
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						HAPPath refPath = new HAPPath();
						HAPElementStructure parentContextEle = resolveInfo.realSolved.finalElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = parentSolidNodeRef.getPath().appendPath(resolveInfo.path.getPath());
						}
						else {
							refRootId = resolveInfo.referredRoot.getId();
							refPath = resolveInfo.path.getPath(); 
						}
						relativeElement.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, refPath));
					}
					
					HAPElementStructure relativeEleDef = relativeElement.getDefinition();
					if(relativeEleDef==null) {
						relativeElement.setDefinition(solvedElement.cloneStructureElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityStructure.mergeElement(solvedElement, relativeEleDef, false, matchers, null, runtimeEnv);
						//remove all the void matchers
						Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
						for(String p : matchers.keySet()){
							HAPMatchers match = matchers.get(p);
							if(!match.isVoid()){
								noVoidMatchers.put(p, match);
							}
						}
						relativeElement.setMatchers(noVoidMatchers);
						
						//merge rule from parent
						HAPUtilityStructure.traverseElement(solvedElement, null, new HAPProcessorStructureElement() {
							@Override
							public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
								if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
									HAPElementStructureLeafData parentDataEle = (HAPElementStructureLeafData)eleInfo.getElement();

									HAPVariableDataInfo solidParentDataInfo = parentDataEle.getDataInfo();
									HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get(eleInfo.getElementPath().getFullName())));
									for(HAPDataRule rule : solidParentDataInfo.getRules()) {
										HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeEleDef).getDataInfo();
										relativeDataInfo.addRule(rule);
										relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
									}
								}
								return null;
							}

							@Override
							public void postProcess(HAPInfoElement eleInfo, Object value) {	}
						}, null);
					}
				}
				else{
					//not find parent node, throw exception
					errors.add(HAPServiceData.createFailureData(eleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
				}
			}
			}			
		}
		return out;
	}
	

//	private static HAPInfoReferenceResolve processReference(HAPInfoPathReference reference, HAPStructure self, HAPContainerStructure parents, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) throws HAPExceptionResolveReference{
//		HAPStructure parentStructure = HAPUtilityStructureElementReference.getReferedStructure(reference.getParent(), parents, self);
//		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(reference.getReferencePath(), parentStructure, configure.elementReferenceResolveMode, configure.relativeInheritRule, null);
//		
//		if(resolveInfo==null || resolveInfo.referredRoot==null) {
//			throw new HAPExceptionResolveReference(resolveInfo);
//		}
//		else {
//			HAPElementStructure solvedContextEle = resolveInfo.resolvedElement; 
//			if(solvedContextEle!=null){
//				//inherit rule from parent
//				if(configure.relativeInheritRule) {
//					HAPElementStructure solidParent = solvedContextEle.getSolidStructureElement();
//					if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
//						HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataInfo();
//						for(HAPDataRule rule : solidParentDataInfo.getRules()) {
//							HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeEleDef).getDataInfo();
//							relativeDataInfo.addRule(rule);
//						}
//					}
//				}
//			}
//			else{
//				//not find parent node, throw exception
//				throw new HAPExceptionResolveReference(resolveInfo);
//			}
//		}
//		return resolveInfo;
//	}


}
