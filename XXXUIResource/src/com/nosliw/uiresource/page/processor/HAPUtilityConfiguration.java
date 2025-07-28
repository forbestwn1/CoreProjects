package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityConfiguration {

	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForUIUit(String type) {
		HAPConfigureProcessorValueStructure out = new HAPConfigureProcessorValueStructure();
		populateCommonConfigure(out);
		if(type.equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) 	out.inheritMode = HAPConstant.INHERITMODE_CHILD;  //for tag, child keeps same
		else out.inheritMode = HAPConstant.INHERITMODE_PARENT;   //for resource, parent overwrite child
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForTagDefinition(HAPValueStructure tagDefValueStructure, HAPConfigureProcessorValueStructure parentConfigure) {
		HAPConfigureProcessorValueStructure out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefValueStructure.getInfo());
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}
 
	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForInternal(HAPConfigureProcessorValueStructure parentConfigure) {
		HAPConfigureProcessorValueStructure out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.parentCategary = HAPValueStructureDefinitionGroup.getAllCategaries();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}
	
	private static void populateCommonConfigure(HAPConfigureProcessorValueStructure configure) {
		configure.relativeInheritRule = true;
		configure.relativeTrackingToSolid = true;
	}
}
