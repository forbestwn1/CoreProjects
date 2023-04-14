package com.nosliw.data.core.structure.temp;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextVariableInheritance {

	//merge with parent through inheritance
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup orgContext, HAPContainerStructure parent, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = processConstant(orgContext);
		for(String parentName : parent.getStructureNames()) {
			out = process(out, (HAPValueStructureDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedStructure(parentName, parent, orgContext), false), inheritMode, inheritanceExcludedInfo, runtimeEnv);
		}
		return out;
	}

	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup orgContext, HAPValueStructureDefinitionGroup parentContextGroup, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = orgContext.cloneValueStructureGroup();
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			for(String categary : HAPValueStructureDefinitionGroup.getAllCategaries()){
				if(parentContextGroup!=null && Arrays.asList(HAPValueStructureDefinitionGroup.getInheritableCategaries()).contains(categary)) {
					HAPValueStructureDefinitionFlat parentContext = parentContextGroup.getFlat(categary);
					Map<String, HAPRootStructure> parentEles = parentContext.getRoots();
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
	private static HAPStructure processConstant(HAPStructure structure) {
		HAPStructure out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			if(root.isConstant()) {
				root.getInfo().setValue(HAPRootStructure.INHERIT_MODE, HAPRootStructure.INHERIT_MODE_FINAL);
			}
		}
		return out;
	}
	
	//whether child can herit from parent element
	private static boolean isInheritable(HAPValueStructureDefinitionGroup childContextGroup, HAPValueStructureDefinitionGroup parentContextGroup, String categary, String eleName, String inheritMode) {
		boolean out = false;
		HAPRootStructure childNode = childContextGroup.getElement(categary, eleName);
		if(childNode==null) 		out = true;
		else {
			if(HAPConstant.INHERITMODE_PARENT.equals(inheritMode)) {
				if(!HAPRootStructure.INHERIT_MODE_FINAL.equals(childNode.getInfo().getValue(HAPRootStructure.INHERIT_MODE_FINAL))) {
					out = true;
				}
			}
		}
		return out;
	}
	
}
