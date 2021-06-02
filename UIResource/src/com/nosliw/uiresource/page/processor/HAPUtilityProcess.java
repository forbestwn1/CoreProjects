package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementLeafConstant;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUtilityUITag;

public class HAPUtilityProcess {

	//build context for ui Tag
	public static HAPValueStructureDefinitionGroup buildUITagContext(HAPUITagDefinition tagDef, HAPValueStructureDefinitionGroup parentContext, Map<String, String> attributes, HAPConfigureProcessorStructure contextProcessorConfig, HAPRuntimeEnvironment runtimeEnv){
		//get contextDef 
		HAPValueStructure tagDefinitionContext = tagDef.getValueStructureDefinition();

		//add attribute constant as part of tagContext
		Map<String, String> tagAttrs = HAPUtilityUITag.getTagAttributeValue(tagDef, attributes);
		HAPValueStructureDefinitionGroup tagContext = (HAPValueStructureDefinitionGroup)tagDefinitionContext.cloneStructure();
		for(String name : tagAttrs.keySet()) {
			HAPElementLeafConstant cstRootNode = new HAPElementLeafConstant(tagAttrs.get(name));
			HAPRoot root = new HAPRoot(cstRootNode);
			root.setName(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + name);
			tagContext.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE, root);
		}
		return HAPProcessorContext.processStatic(tagContext, HAPContainerStructure.createDefault(parentContext), null, null, HAPUtilityConfiguration.getContextProcessConfigurationForTagDefinition(tagDefinitionContext, contextProcessorConfig), runtimeEnv);
	}
}
