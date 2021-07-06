package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.attachment.HAPInfoAttachment;
import com.nosliw.data.core.component.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.structure.HAPParserStructure;

public class HAPProcessorAttachmentEntityValueStructure implements HAPProcessorAttachmentEntity{

	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity) {
		return HAPParserStructure.parseRoots(attachmentInfo.getEntity());
	}

}
