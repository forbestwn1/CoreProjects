package com.nosliw.data.core.complex.attachment;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public interface HAPProcessorAttachmentEntity {

	//parse entity in attachment
	Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity);

}