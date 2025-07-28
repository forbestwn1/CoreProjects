package com.nosliw.data.core.valuestructure1;

import com.nosliw.core.application.common.structure.HAPParserStructure;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment1.HAPInfoAttachment;

public class HAPProcessorAttachmentEntityValueStructure implements HAPProcessorAttachmentEntity{

	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPManualBrickComplex complexEntity) {
		return HAPParserStructure.parseStructureRoots(attachmentInfo.getEntity());
	}

}
