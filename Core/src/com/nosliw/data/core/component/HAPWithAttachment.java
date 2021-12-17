package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPWithAttachment {

	@HAPAttribute
	public static final String ATTACHMENT = "attachment";

	HAPIdEntityInDomain getAttachmentContainerId();

	void setAttachmentContainerId(HAPIdEntityInDomain attachmentId);

}
