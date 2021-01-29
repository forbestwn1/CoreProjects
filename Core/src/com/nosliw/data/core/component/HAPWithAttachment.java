package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;

public interface HAPWithAttachment {

	public static final String ATTACHMENT = "attachment";
	
	HAPAttachmentContainer getAttachmentContainer();
	void setAttachmentContainer(HAPAttachmentContainer attachmentContainer);
	
	Map<String, HAPAttachment> getAttachmentsByType(String type);

	HAPLocalReferenceBase getLocalReferenceBase();

	void setLocalReferenceBase(HAPLocalReferenceBase localRefBase);

	void mergeBy(HAPWithAttachment parent, String mode);

	void cloneToAttachment(HAPWithAttachment withAttachment);
}
