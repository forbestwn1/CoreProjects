package com.nosliw.data.core.domain.entity.attachment;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;

public interface HAPProcessorAttachmentEntity {

	//parse entity in attachment
	Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityInDomainComplex complexEntity);

}
