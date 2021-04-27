package com.nosliw.data.core.structure;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public class HAPProcessorContextVariableInheritance {

	//merge with parent through inheritance
	public static HAPContextStructureValueDefinitionGroup process(HAPContextStructureValueDefinitionGroup orgContext, HAPParentContext parent, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextStructureValueDefinitionGroup out = processConstant(orgContext);
		for(String parentName : parent.getNames()) {
			out = process(out, (HAPContextStructureValueDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedContext(parentName, parent, orgContext), false), inheritMode, inheritanceExcludedInfo, runtimeEnv);
		}
		return out;
	}

	public static HAPContextStructureValueDefinitionGroup process(HAPContextStructureValueDefinitionGroup orgContext, HAPContextStructureValueDefinitionGroup parentContextGroup, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextStructureValueDefinitionGroup out = orgContext.cloneContextGroup();
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			for(String categary : HAPContextStructureValueDefinitionGroup.getAllContextTypes()){
				if(parentContextGroup!=null && Arrays.asList(HAPContextStructureValueDefinitionGroup.getInheritableContextTypes()).contains(categary)) {
					HAPContextStructureValueDefinitionFlat parentContext = parentContextGroup.getContext(categary);
					Map<String, HAPRoot> parentEles = parentContext.getElements();
					for(String eleName : parentEles.keySet()) {
						if(isInheritable(out, parentContextGroup, categary, eleName, inheritMode)) {
							out.addElement(eleName, HAPUtilityContext.createRelativeContextDefinitionRoot(parentContextGroup, categary, eleName, inheritanceExcludedInfo), categary);
						}
					}
				}
			}
		}
		return out;
	} 

	//add FINAL to all constant root node, means constant cannot be override by parent 
	private static HAPContextStructureValueDefinitionGroup processConstant(HAPContextStructureValueDefinitionGroup contextGroup) {
		HAPContextStructureValueDefinitionGroup out = contextGroup.cloneContextGroup();
		for(String contextCategary : HAPContextStructureValueDefinitionGroup.getAllContextTypes()) {
			for(String name : out.getContext(contextCategary).getElementNames()) {
				HAPRoot node = out.getElement(contextCategary, name);
				if(node.isConstant()) {
					node.getInfo().setValue(HAPRoot.INHERIT_MODE, HAPRoot.INHERIT_MODE_FINAL);
				}
			}
		}
		return out;
	}
	
	//whether child can herit from parent element
	private static boolean isInheritable(HAPContextStructureValueDefinitionGroup childContextGroup, HAPContextStructureValueDefinitionGroup parentContextGroup, String categary, String eleName, String inheritMode) {
		boolean out = false;
		HAPRoot childNode = childContextGroup.getElement(categary, eleName);
		if(childNode==null) 		out = true;
		else {
			if(HAPConstant.INHERITMODE_PARENT.equals(inheritMode)) {
				if(!HAPRoot.INHERIT_MODE_FINAL.equals(childNode.getInfo().getValue(HAPRoot.INHERIT_MODE_FINAL))) {
					out = true;
				}
			}
		}
		return out;
	}
	
}
