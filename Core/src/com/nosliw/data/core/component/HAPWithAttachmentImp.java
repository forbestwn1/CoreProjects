package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;

public class HAPWithAttachmentImp extends HAPEntityInfoWritableImp implements HAPWithAttachment{
	
	private HAPContainerAttachment m_attachmentContainer;

	public HAPWithAttachmentImp() {
		this.m_attachmentContainer = new HAPContainerAttachment();
	}

	@Override
	public HAPContainerAttachment getAttachmentContainer() {		return this.m_attachmentContainer;	}
	@Override
	public void setAttachmentContainer(HAPContainerAttachment attachmentContainer) {  this.m_attachmentContainer = attachmentContainer;}  

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {	return this.m_attachmentContainer.getAttachmentByType(type);	}

	@Override
	public HAPAttachment getAttachment(String type, String name) {
		HAPAttachment out = null;
		Map<String, HAPAttachment> atts = this.getAttachmentsByType(type);
		if(atts!=null)  out = atts.get(name);
		return out;
	}

	@Override
	public HAPLocalReferenceBase getLocalReferenceBase() {    return this.m_attachmentContainer.getLocalReferenceBase(); 	}

	@Override
	public void setLocalReferenceBase(HAPLocalReferenceBase localRefBase) {   this.m_attachmentContainer.setLocalReferenceBase(localRefBase); }

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {	this.m_attachmentContainer.merge(parent.getAttachmentContainer(), mode);	}

	@Override
	public void cloneToAttachment(HAPWithAttachment withAttachment) {
		withAttachment.setAttachmentContainer(this.m_attachmentContainer.cloneAttachmentContainer());
	}
}
