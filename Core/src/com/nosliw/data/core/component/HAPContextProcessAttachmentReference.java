package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPContextProcessAttachmentReference {

	private String m_attachmentValueType;
	
	private HAPDefinitionEntityComplex m_complexEntity;
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPContextProcessAttachmentReference(String attachmentValueType, HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		this.m_attachmentValueType = attachmentValueType;
		this.m_runtimeEnv = runtimeEnv;
		this.m_complexEntity = complexEntity;
	}
	
	public HAPResultProcessAttachmentReference processReference(String attachmentName) {
		HAPAttachment attachment = this.m_complexEntity.getAttachment(this.m_attachmentValueType, attachmentName);
		String attType = attachment.getType();
		Object entity = null;
		HAPDefinitionEntityComplex contextComplexEntity = null;
		if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)) {
			entity = this.processEntityAttachment(attachmentName);
			contextComplexEntity = this.m_complexEntity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)) {
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId());
			if(entity instanceof HAPDefinitionEntityComplex)  contextComplexEntity = (HAPDefinitionEntityComplex)entity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)) {
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId());
			if(entity instanceof HAPDefinitionEntityComplex)  contextComplexEntity = (HAPDefinitionEntityComplex)entity;
		}
		
		return new HAPResultProcessAttachmentReference(entity, attachment.getAdaptor(), contextComplexEntity);
	}
	
	abstract protected Object processEntityAttachment(String attachmentName);
	
	protected HAPDefinitionEntityComplex getComplexEntity() {    return this.m_complexEntity;    }
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }

}
