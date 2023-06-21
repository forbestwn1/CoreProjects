package com.nosliw.data.core.domain.entity.attachment;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.attachment1.HAPInfoAttachment;

public interface HAPProcessorAttachmentEntity {

	//parse entity in attachment
	Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityInDomainComplex complexEntity);

}
