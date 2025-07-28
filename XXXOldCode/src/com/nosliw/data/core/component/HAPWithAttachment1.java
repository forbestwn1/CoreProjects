package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

public interface HAPWithAttachment1 {

	public static final String ATTACHMENT = "attachment";

	//all attachments
	HAPDefinitionEntityContainerAttachment getAttachmentContainer();
	void setAttachmentContainer(HAPDefinitionEntityContainerAttachment attachmentContainer);
	
	Map<String, HAPAttachment> getAttachmentsByType(String type);
	HAPAttachment getAttachment(String type, String name);
	HAPAttachment getAttachment(HAPReferenceAttachment idAttachment);

	//
	void mergeBy(HAPWithAttachment1 parent, String mode);

	void cloneToAttachment(HAPWithAttachment1 withAttachment);
}
