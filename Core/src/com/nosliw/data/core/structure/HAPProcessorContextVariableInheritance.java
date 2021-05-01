package com.nosliw.data.core.structure;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPProcessorContextVariableInheritance {

	//merge with parent through inheritance
	public static HAPStructureValueDefinitionGroup process(HAPStructureValueDefinitionGroup orgContext, HAPParentContext parent, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructureValueDefinitionGroup out = processConstant(orgContext);
		for(String parentName : parent.getNames()) {
			out = process(out, (HAPStructureValueDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedContext(parentName, parent, orgContext), false), inheritMode, inheritanceExcludedInfo, runtimeEnv);
		}
		return out;
	}

	public static HAPStructureValueDefinitionGroup process(HAPStructureValueDefinitionGroup orgContext, HAPStructureValueDefinitionGroup parentContextGroup, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructureValueDefinitionGroup out = orgContext.cloneContextGroup();
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			for(String categary : HAPStructureValueDefinitionGroup.getAllCategaries()){
				if(parentContextGroup!=null && Arrays.asList(HAPStructureValueDefinitionGroup.getInheritableCategaries()).contains(categary)) {
					HAPStructureValueDefinitionFlat parentContext = parentContextGroup.getFlat(categary);
					Map<String, HAPRoot> parentEles = parentContext.getRoots();
					for(String eleName : parentEles.keySet()) {
						if(isInheritable(out, parentContextGroup, categary, eleName, inheritMode)) {
							out.addRoot(eleName, HAPUtilityContext.createRelativeContextDefinitionRoot(parentContextGroup, categary, eleName, inheritanceExcludedInfo), categary);
						}
					}
				}
			}
		}
		return out;
	} 

	//add FINAL to all constant root node, means constant cannot be override by parent 
	private static HAPStructureValueDefinitionGroup processConstant(HAPStructureValueDefinitionGroup contextGroup) {
		HAPStructureValueDefinitionGroup out = contextGroup.cloneContextGroup();
		for(String contextCategary : HAPStructureValueDefinitionGroup.getAllCategaries()) {
			for(String name : out.getFlat(contextCategary).getRootNames()) {
				HAPRoot node = out.getElement(contextCategary, name);
				if(node.isConstant()) {
					node.getInfo().setValue(HAPRoot.INHERIT_MODE, HAPRoot.INHERIT_MODE_FINAL);
				}
			}
		}
		return out;
	}
	
	//whether child can herit from parent element
	private static boolean isInheritable(HAPStructureValueDefinitionGroup childContextGroup, HAPStructureValueDefinitionGroup parentContextGroup, String categary, String eleName, String inheritMode) {
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
