package com.nosliw.uiresource.page.processor;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.tag.HAPUITagDefinitionContext;

public class HAPUtilityConfiguration {

	public static HAPConfigureContextProcessor getContextProcessConfigurationForUIUit(HAPExecutableUIUnit uiUnit) {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		if(uiUnit.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) 	out.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;  //for tag, child keeps same
		else out.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;   //for resource, parent overwrite child
		return out;
	}

	public static HAPConfigureContextProcessor getContextProcessConfigurationForTagDefinition(HAPUITagDefinitionContext tagDefinitionContext, HAPConfigureContextProcessor parentConfigure) {
		HAPConfigureContextProcessor configure = parentConfigure.cloneConfigure();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefinitionContext.getInfo());
		return configure;
	}

	public static HAPConfigureContextProcessor getContextProcessConfigurationForInternal(HAPConfigureContextProcessor parentConfigure) {
		HAPConfigureContextProcessor out = parentConfigure.cloneConfigure();
		out.parentCategary = HAPContextGroup.getAllContextTypes();
		return out;
	}
}
