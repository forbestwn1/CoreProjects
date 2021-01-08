package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.tag.HAPContextUITagDefinition;

public class HAPUtilityConfiguration {

	public static HAPConfigureContextProcessor getContextProcessConfigurationForUIUit(String type) {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		populateCommonConfigure(out);
		if(type.equals(HAPConstant.UIRESOURCE_TYPE_TAG)) 	out.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;  //for tag, child keeps same
		else out.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;   //for resource, parent overwrite child
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	public static HAPConfigureContextProcessor getContextProcessConfigurationForTagDefinition(HAPContextUITagDefinition tagDefinitionContext, HAPConfigureContextProcessor parentConfigure) {
		HAPConfigureContextProcessor out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefinitionContext.getInfo());
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	public static HAPConfigureContextProcessor getContextProcessConfigurationForInternal(HAPConfigureContextProcessor parentConfigure) {
		HAPConfigureContextProcessor out = parentConfigure.cloneConfigure();
		populateCommonConfigure(out);
		out.parentCategary = HAPContextGroup.getAllContextTypes();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}
	
	private static void populateCommonConfigure(HAPConfigureContextProcessor configure) {
		configure.relativeInheritRule = true;
		configure.relativeTrackingToSolid = true;
	}
}
