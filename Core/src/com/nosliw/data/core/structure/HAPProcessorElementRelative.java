package com.nosliw.data.core.structure;

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
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPProcessorElementRelative {

	public static HAPStructure process(HAPStructure structure, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return process(structure, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}

	public static HAPStructure process(HAPStructure structure, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructure out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
						String parent = relativeEle.getParent();
						if(dependency!=null)   dependency.add(parent);
						if(!relativeEle.isProcessed()) {
							HAPStructure parentStructure = HAPUtilityStructure.getReferedStructure(parent, parents, structure);
							return Pair.of(false, processRelativeElement(relativeEle, parentStructure, errors, configure, runtimeEnv));
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
	
	public static HAPRootStructure process(HAPRootStructure root, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPRootStructure out = root.cloneRoot();
		HAPUtilityStructure.traverseElement(out, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
					String parent = relativeEle.getParent();
					if(dependency!=null)   dependency.add(parent);
					if(!relativeEle.isProcessed()) {
						HAPStructure parentStructure = HAPUtilityStructure.getReferedStructure(parent, parents, null);
						return Pair.of(false, processRelativeElement(relativeEle, parentStructure, errors, configure, runtimeEnv));
					}
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		return out;
	}
	
	private static HAPElementStructure processRelativeElement(HAPElementStructureLeafRelative relativeElement, HAPStructure parentStructure, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructure out = relativeElement;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructure.resolveElementReference(relativeElement.getReferencePath(), parentStructure, configure.elementReferenceResolveMode, null);
		
		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(relativeElement, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getType()) {
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
				HAPElementStructure solvedContextEle = resolveInfo.resolvedElement; 
				if(solvedContextEle!=null){
					relativeElement.setResolvedIdPath(resolveInfo.path);
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						HAPPath refPath = new HAPPath();
						HAPElementStructure parentContextEle = resolveInfo.realSolved.resolvedElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = parentSolidNodeRef.getPath().appendPath(resolveInfo.path.getPath());
						}
						else {
							refRootId = resolveInfo.referredRoot.getLocalId();
							refPath = resolveInfo.path.getPath(); 
						}
						relativeElement.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, refPath));
					}
					
					HAPElementStructure relativeEleDef = relativeElement.getDefinition();
					if(relativeEleDef==null) {
						relativeElement.setDefinition(solvedContextEle.cloneStructureElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityStructure.mergeElement(solvedContextEle, relativeEleDef, false, matchers, null, runtimeEnv);
						//remove all the void matchers
						Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
						for(String p : matchers.keySet()){
							HAPMatchers match = matchers.get(p);
							if(!match.isVoid()){
								noVoidMatchers.put(p, match);
							}
						}
						relativeElement.setMatchers(noVoidMatchers);
						
						//inherit rule from parent
						if(configure.relativeInheritRule) {
							HAPElementStructure solidParent = solvedContextEle.getSolidStructureElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeEleDef).getDataInfo();
									relativeDataInfo.addRule(rule);
									relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
								}
							}
						}
					}
				}
				else{
					//not find parent node, throw exception
					errors.add(HAPServiceData.createFailureData(relativeElement, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!configure.tolerantNoParentForRelative)  throw new RuntimeException();
				}
			}
			}			
		}
		return out;
	}
	
}
