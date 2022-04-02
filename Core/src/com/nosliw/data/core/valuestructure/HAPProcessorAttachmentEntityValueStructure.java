package com.nosliw.data.core.valuestructure;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPInfoAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.structure.HAPParserStructure;

public class HAPProcessorAttachmentEntityValueStructure implements HAPProcessorAttachmentEntity{

	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityInDomainComplex complexEntity) {
		return HAPParserStructure.parseRoots(attachmentInfo.getEntity());
	}

}
