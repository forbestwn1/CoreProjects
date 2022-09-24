package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededWithId;

public interface HAPWithAttachment {

	@HAPAttribute
	public static final String ATTACHMENT = "attachment";

	HAPEmbededWithId getAttachmentContainerEntity();

	void setAttachmentContainerEntity(HAPEmbededWithId attachmentEntity);

}
