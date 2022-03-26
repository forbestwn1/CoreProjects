package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPEmbededEntity;

public interface HAPWithAttachment {

	@HAPAttribute
	public static final String ATTACHMENT = "attachment";

	HAPEmbededEntity getAttachmentContainerEntity();

	void setAttachmentContainerEntity(HAPEmbededEntity attachmentEntity);

}
