package com.nosliw.uiresource.page.processor;

import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPProcessorAttachment {

	public static void processAttachment(HAPDefinitionUIUnit uiUnitDef, HAPContainerAttachment parentAttachment, HAPManagerUITag uiTagMan) {
		
		//merge with parent
		HAPUtilityComponent.mergeWithParentAttachment(uiUnitDef, parentAttachment);

		//process child tags
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			processAttachment(uiTag, uiUnitDef.getAttachmentContainer(), uiTagMan);
		}
	}
}
