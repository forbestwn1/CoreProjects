package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPInfoAttachment;
import com.nosliw.data.core.component.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPContextProcessor {

	private HAPDefinitionEntityComplex m_complexEntity;
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPContextProcessor(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_complexEntity = complexEntity;
	}
	
	public HAPResultProcessAttachmentReference processAttachmentReference(String attachmentValueType, String attachmentName) {
		HAPAttachment attachment = this.m_complexEntity.getAttachment(attachmentValueType, attachmentName);
		String attType = attachment.getType();
		Object entity = null;
		HAPDefinitionEntityComplex contextComplexEntity = null;
		if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)) {
			entity = this.m_runtimeEnv.getAttachmentManager().parseEntityAttachment(new HAPInfoAttachment(attachmentValueType, attachmentName, ((HAPAttachmentEntity)attachment).getEntity()), this.m_complexEntity);
			contextComplexEntity = this.m_complexEntity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)) {
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId());
			if(entity instanceof HAPDefinitionEntityComplex)  contextComplexEntity = (HAPDefinitionEntityComplex)entity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)) {
			HAPResourceDefinition relatedResource = null;
			if(m_complexEntity instanceof HAPResourceDefinition)   relatedResource = (HAPResourceDefinition)m_complexEntity;
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId(), relatedResource);
			if(entity instanceof HAPDefinitionEntityComplex)  contextComplexEntity = (HAPDefinitionEntityComplex)entity;
		}
		
		return new HAPResultProcessAttachmentReference(entity, attachment.getAdaptor(), contextComplexEntity);
	}
	
	public HAPDefinitionEntityComplex getComplexEntity() {    return this.m_complexEntity;    }
	
	public HAPWrapperValueStructure getValueStructureWrapper() {   return this.m_complexEntity==null?null:this.m_complexEntity.getValueStructureWrapper();     }
	
	public HAPContainerAttachment getAttachmentContainer() {   return this.m_complexEntity==null?null:this.m_complexEntity.getAttachmentContainer();    }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }

}
