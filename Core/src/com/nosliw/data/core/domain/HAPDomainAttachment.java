package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

public class HAPDomainAttachment {

	private Map<HAPIdEntityInDomain, HAPDefinitionEntityContainerAttachment> m_attachmentContainer;
	
	public HAPDomainAttachment() {
		this.m_attachmentContainer = new LinkedHashMap<HAPIdEntityInDomain, HAPDefinitionEntityContainerAttachment>();
	}
	
	public void addAttachmentContainer(HAPDefinitionEntityContainerAttachment attachmentContainer, HAPIdEntityInDomain complexId) {
		this.m_attachmentContainer.put(complexId, attachmentContainer.cloneAttachmentContainer());
	}
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer(HAPIdEntityInDomain complexeId) {
		return this.m_attachmentContainer.get(complexeId);
	}
	
}
