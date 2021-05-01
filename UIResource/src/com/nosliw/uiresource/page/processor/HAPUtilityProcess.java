package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPProcessorContext;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;
import com.nosliw.uiresource.page.tag.HAPContextUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUtilityUITag;

public class HAPUtilityProcess {

	//build context for ui Tag
	public static HAPStructureValueDefinitionGroup buildUITagContext(HAPUITagDefinition tagDef, HAPStructureValueDefinitionGroup parentContext, Map<String, String> attributes, HAPConfigureProcessorStructure contextProcessorConfig, HAPRuntimeEnvironment runtimeEnv){
		//get contextDef 
		HAPContextUITagDefinition tagDefinitionContext = tagDef.getContext();

		//add attribute constant as part of tagContext
		Map<String, String> tagAttrs = HAPUtilityUITag.getTagAttributeValue(tagDef, attributes);
		HAPStructureValueDefinitionGroup tagContext = tagDefinitionContext.cloneContextGroup();
		for(String name : tagAttrs.keySet()) {
			HAPElementLeafConstant cstRootNode = new HAPElementLeafConstant(tagAttrs.get(name));
			tagContext.addRoot(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + name, new HAPRoot(cstRootNode), HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		return HAPProcessorContext.processStatic(tagContext, HAPParentContext.createDefault(parentContext), null, null, HAPUtilityConfiguration.getContextProcessConfigurationForTagDefinition(tagDefinitionContext, contextProcessorConfig), runtimeEnv);
	}
}
