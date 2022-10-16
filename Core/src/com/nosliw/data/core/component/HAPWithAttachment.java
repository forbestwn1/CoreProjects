package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededWithIdDefinition;

public interface HAPWithAttachment {

	@HAPAttribute
	public static final String ATTACHMENT = "attachment";

	HAPEmbededWithIdDefinition getAttachmentContainerEntity();

	void setAttachmentContainerEntity(HAPEmbededWithIdDefinition attachmentEntity);

}
