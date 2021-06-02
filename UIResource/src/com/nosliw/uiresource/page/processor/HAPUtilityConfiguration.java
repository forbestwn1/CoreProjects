package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityConfiguration {

	public static HAPConfigureProcessorStructure getContextProcessConfigurationForUIUit(String type) {
		HAPConfigureProcessorStructure out = new HAPConfigureProcessorStructure();
		populateCommonConfigure(out);
		if(type.equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) 	out.inheritMode = HAPConstant.INHERITMODE_CHILD;  //for tag, child keeps same
		else out.inheritMode = HAPConstant.INHERITMODE_PARENT;   //for resource, parent overwrite child
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	public static HAPConfigureProcessorStructure getContextProcessConfigurationForTagDefinition(HAPValueStructure tagDefValueStructure, HAPConfigureProcessorStructure parentConfigure) {
		HAPConfigureProcessorStructure out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefValueStructure.getInfo());
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}
 
	public static HAPConfigureProcessorStructure getContextProcessConfigurationForInternal(HAPConfigureProcessorStructure parentConfigure) {
		HAPConfigureProcessorStructure out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.parentCategary = HAPValueStructureDefinitionGroup.getAllCategaries();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}
	
	private static void populateCommonConfigure(HAPConfigureProcessorStructure configure) {
		configure.relativeInheritRule = true;
		configure.relativeTrackingToSolid = true;
	}
}
