package com.nosliw.data.core.component.attachment;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;

public interface HAPProcessorAttachmentEntity {

	//parse entity in attachment
	Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity);

}
