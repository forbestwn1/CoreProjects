package com.nosliw.data.core.structure;

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

	
	public static HAPStructure process(HAPStructure structure, HAPContainerStructure parents, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructure out = structure.cloneStructure();
		for(HAPRoot root : out.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)eleInfo.getElement();
						String parent = relativeEle.getParent();
						dependency.add(parent);
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
	
	
	private static HAPElement processRelativeElement(HAPElementLeafRelative relativeElement, HAPStructure parentStructure, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPElement out = relativeElement;
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
				out = new HAPElementLeafConstant();
				Object constantValue = ((HAPElementLeafConstant)resolveInfo.referredRoot.getDefinition()).getValue();
				((HAPElementLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				HAPElement solvedContextEle = resolveInfo.resolvedElement; 
				if(solvedContextEle!=null){
					relativeElement.setResolvedIdPath(resolveInfo.path);
					//refer to solid
					if(configure.relativeTrackingToSolid) {
						String refRootId = null;
						HAPPath refPath = new HAPPath();
						HAPElement parentContextEle = resolveInfo.realSolved.resolvedElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = parentSolidNodeRef.getPath().appendPath(resolveInfo.path.getPath());
						}
						else {
							refRootId = resolveInfo.referredRoot.getLocalId();
							refPath = resolveInfo.path.getPath(); 
						}
						relativeElement.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, refPath));
					}
					
					HAPElement relativeEleDef = relativeElement.getDefinition();
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
							HAPElement solidParent = solvedContextEle.getSolidStructureElement();
							if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
								HAPVariableDataInfo solidParentDataInfo = ((HAPElementLeafData)solidParent).getDataInfo();
								HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
								for(HAPDataRule rule : solidParentDataInfo.getRules()) {
									HAPVariableDataInfo relativeDataInfo = ((HAPElementLeafData)relativeEleDef).getDataInfo();
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
