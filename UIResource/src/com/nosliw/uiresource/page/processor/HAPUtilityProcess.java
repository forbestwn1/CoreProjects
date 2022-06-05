package com.nosliw.uiresource.page.processor;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPProcessorStructure;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUtilityUITag;

public class HAPUtilityProcess {

	//build context for ui Tag
	public static HAPValueStructureDefinitionGroup buildUITagValueStructure(HAPUITagDefinition tagDef, HAPValueStructureDefinitionGroup parentValueStructure, Map<String, String> attributes, HAPConfigureProcessorValueStructure contextProcessorConfig, HAPRuntimeEnvironment runtimeEnv){
		//get contextDef 
		HAPValueStructure tagValueStructure = tagDef.getValueStructureDefinition();

		//add attribute constant as part of tagContext
		Map<String, String> tagAttrs = HAPUtilityUITag.getTagAttributeValue(tagDef, attributes);
		HAPValueStructureDefinitionGroup tagValueStructureNew = (HAPValueStructureDefinitionGroup)tagValueStructure.cloneStructure();
		for(String name : tagAttrs.keySet()) {
			HAPElementStructureLeafConstant cstRootNode = new HAPElementStructureLeafConstant(tagAttrs.get(name));
			HAPRootStructure root = new HAPRootStructure(cstRootNode);
			root.setName(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + name);
			tagValueStructureNew.addRootToCategary(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE, root);
		}
		return (HAPValueStructureDefinitionGroup)HAPProcessorStructure.processStatic(tagValueStructureNew, HAPContainerStructure.createDefault(parentValueStructure), null, null, HAPUtilityConfiguration.getContextProcessConfigurationForTagDefinition(tagValueStructure, contextProcessorConfig), runtimeEnv);
	}
}
