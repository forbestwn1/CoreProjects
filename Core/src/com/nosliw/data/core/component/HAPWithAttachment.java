package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithId;

public interface HAPWithAttachment {

	@HAPAttribute
	public static final String ATTACHMENT = "attachment";

	HAPEmbededDefinitionWithId getAttachmentContainerEntity();

	void setAttachmentContainerEntity(HAPEmbededDefinitionWithId attachmentEntity);

}
