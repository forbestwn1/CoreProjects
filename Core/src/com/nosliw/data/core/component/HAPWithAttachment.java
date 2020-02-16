package com.nosliw.data.core.component;

import java.util.Map;

public interface HAPWithAttachment {

	public static final String ATTACHMENT = "attachment";
	
	HAPAttachmentContainer getAttachmentContainer();
	void setAttachmentContainer(HAPAttachmentContainer attachmentContainer);
	
	Map<String, HAPAttachment> getAttachmentsByType(String type);
	
	void mergeBy(HAPWithAttachment parent, String mode);

	void cloneToAttachment(HAPWithAttachment withAttachment);
}
