package com.nosliw.uiresource.page.processor;

import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

public class HAPProcessorAttachment {

	public static void mergeAttachment(HAPDefinitionUIUnit uiUnitDef, HAPDefinitionUIUnit parentUiUnitDef) {
		
		//merge with parent
		HAPUtilityComponent.processAttachmentInChild(uiUnitDef, parentUiUnitDef);
		
		//process child tags
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			mergeAttachment(uiTag, uiUnitDef);
		}
	}
}
