package com.nosliw.data.core.structure;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPProcessorElementVariableInheritance {

	//merge with parent through inheritance
	public static HAPValueStructure process(HAPValueStructure orgContext, HAPContainerStructure parent, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructure out = (HAPValueStructure)processConstant(orgContext);
		for(String parentName : parent.getStructureNames()) {
//			out = process(out, (HAPValueStructureDefinitionGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedStructure(parentName, parent, orgContext), false), inheritMode, inheritanceExcludedInfo, runtimeEnv);
			out = process(out, (HAPValueStructure)HAPUtilityStructureElementReference.getReferedStructure(parentName, parent, orgContext), inheritMode, inheritanceExcludedInfo, runtimeEnv);
		}
		return out;
	}

	public static HAPValueStructure process(HAPValueStructure orgValueStructure, HAPValueStructure parentValueStructure, String inheritMode, Set<String> inheritanceExcludedInfo, HAPRuntimeEnvironment runtimeEnv) {
		if(parentValueStructure==null)   return orgValueStructure;
		HAPValueStructure out = (HAPValueStructure)orgValueStructure.cloneStructure();
		
		if(!HAPConstant.INHERITMODE_NONE.equals(inheritMode)) {
			for(HAPRootStructure root : parentValueStructure.getAllRoots()) {
				if(parentValueStructure.isInheriable(root.getLocalId())) {
					HAPReferenceRootInStrucutre refToParent = parentValueStructure.getRootReferenceById(root.getLocalId());
					if(isInheritable(out, refToParent, inheritMode)) {
						HAPRootStructure newRootInChild = HAPUtilityStructure.createRootWithRelativeElement(root, refToParent.toStringValue(HAPSerializationFormat.LITERATE), inheritanceExcludedInfo);
						out.addRoot(refToParent, newRootInChild);
					}
				}
			}
		}
		return out;
	} 

	//add FINAL to all constant root node, means constant cannot be override by parent 
	private static HAPStructure1 processConstant(HAPStructure1 structure) {
		HAPStructure1 out = structure.cloneStructure();
		for(HAPRootStructure root : out.getAllRoots()) {
			if(root.isConstant()) {
				root.getInfo().setValue(HAPRootStructure.INHERIT_MODE, HAPRootStructure.INHERIT_MODE_FINAL);
			}
		}
		return out;
	}
	
	//whether child can herit from parent element
	private static boolean isInheritable(HAPValueStructure childStructure, HAPReferenceRootInStrucutre rootRef, String inheritMode) {
		boolean out = false;
		HAPRootStructure childNode = HAPUtilityStructure.getRootFromStructure(childStructure, rootRef); 
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
