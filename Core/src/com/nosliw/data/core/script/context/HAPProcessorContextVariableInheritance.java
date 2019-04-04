package com.nosliw.data.core.script.context;

import java.util.Arrays;
import java.util.Map;

public class HAPProcessorContextVariableInheritance {

	//merge with parent through inheritance
	public static HAPContextGroup process(HAPContextGroup orgContext, HAPParentContext parent, String inheritMode, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextGroup out = processConstant(orgContext);
		for(String parentName : parent.getNames()) {
			out = process(out, (HAPContextGroup)HAPUtilityContextStructure.toSolidContextStructure(HAPUtilityContext.getReferedContext(parentName, parent, orgContext), false), inheritMode, contextProcessRequirement);
		}
		return out;
	}

	public static HAPContextGroup process(HAPContextGroup orgContext, HAPContextGroup parentContextGroup, String inheritMode, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextGroup out = orgContext.cloneContextGroup();
		if(!HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE.equals(inheritMode)) {
			for(String categary : HAPContextGroup.getAllContextTypes()){
				if(parentContextGroup!=null && Arrays.asList(HAPContextGroup.getInheritableContextTypes()).contains(categary)) {
					HAPContext parentContext = parentContextGroup.getContext(categary);
					Map<String, HAPContextDefinitionRoot> parentEles = parentContext.getElements();
					for(String eleName : parentEles.keySet()) {
						if(isInheritable(out, parentContextGroup, categary, eleName, inheritMode)) {
							out.addElement(eleName, HAPUtilityContext.createRelativeContextDefinitionRoot(parentContextGroup, categary, eleName, contextProcessRequirement.inheritanceExcludedInfo), categary);
						}
					}
				}
			}
		}
		return out;
	}

	//add FINAL to all constant root node, means constant cannot be override by parent 
	private static HAPContextGroup processConstant(HAPContextGroup contextGroup) {
		HAPContextGroup out = contextGroup.cloneContextGroup();
		for(String contextCategary : HAPContextGroup.getAllContextTypes()) {
			for(String name : out.getContext(contextCategary).getElementNames()) {
				HAPContextDefinitionRoot node = out.getElement(contextCategary, name);
				if(node.isConstant()) {
					node.getInfo().setValue(HAPContextDefinitionRoot.INHERIT_MODE, HAPContextDefinitionRoot.INHERIT_MODE_FINAL);
				}
			}
		}
		return out;
	}
	
	//whether child can herit from parent element
	private static boolean isInheritable(HAPContextGroup childContextGroup, HAPContextGroup parentContextGroup, String categary, String eleName, String inheritMode) {
		boolean out = false;
		HAPContextDefinitionRoot childNode = childContextGroup.getElement(categary, eleName);
		if(childNode==null) 		out = true;
		else {
			if(HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT.equals(inheritMode)) {
				if(!HAPContextDefinitionRoot.INHERIT_MODE_FINAL.equals(childNode.getInfo().getValue(HAPContextDefinitionRoot.INHERIT_MODE_FINAL))) {
					out = true;
				}
			}
		}
		return out;
	}
	
}
