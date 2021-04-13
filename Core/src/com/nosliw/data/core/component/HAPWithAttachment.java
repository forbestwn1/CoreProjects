package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPIdAttachment;

public interface HAPWithAttachment {

	public static final String ATTACHMENT = "attachment";

	//all attachments
	HAPContainerAttachment getAttachmentContainer();
	void setAttachmentContainer(HAPContainerAttachment attachmentContainer);
	
	Map<String, HAPAttachment> getAttachmentsByType(String type);
	HAPAttachment getAttachment(String type, String name);
	HAPAttachment getAttachment(HAPIdAttachment idAttachment);

	//path base for local resource reference
	HAPLocalReferenceBase getLocalReferenceBase();

	void setLocalReferenceBase(HAPLocalReferenceBase localRefBase);

	//
	void mergeBy(HAPWithAttachment parent, String mode);

	void cloneToAttachment(HAPWithAttachment withAttachment);
}
