package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.uiresource.page.tag.HAPContextUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUtilityUITag;

public class HAPUtilityProcess {

	//build context for ui Tag
	public static HAPContextGroup buildUITagContext(HAPUITagDefinition tagDef, HAPContextGroup parentContext, Map<String, String> attributes, HAPConfigureContextProcessor contextProcessorConfig, HAPRuntimeEnvironment runtimeEnv){
		//get contextDef 
		HAPContextUITagDefinition tagDefinitionContext = tagDef.getContext();

		//add attribute constant as part of tagContext
		Map<String, String> tagAttrs = HAPUtilityUITag.getTagAttributeValue(tagDef, attributes);
		HAPContextGroup tagContext = tagDefinitionContext.cloneContextGroup();
		for(String name : tagAttrs.keySet()) {
			HAPContextDefinitionLeafConstant cstRootNode = new HAPContextDefinitionLeafConstant(tagAttrs.get(name));
			tagContext.addElement(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + name, new HAPContextDefinitionRoot(cstRootNode), HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		return HAPProcessorContext.processStatic(tagContext, HAPParentContext.createDefault(parentContext), HAPUtilityConfiguration.getContextProcessConfigurationForTagDefinition(tagDefinitionContext, contextProcessorConfig), runtimeEnv);
	}
}
