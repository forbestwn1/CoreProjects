package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.attachment.HAPAttachment;
import com.nosliw.data.core.complex.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.complex.attachment.HAPAttachmentReference;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.complex.attachment.HAPInfoAttachment;
import com.nosliw.data.core.complex.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessor {

	//domain
	private HAPContextDomain m_domainContext;
	
	//runtime
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	//current complexe entity id 
//	private HAPIdEntityInDomain m_complexEntityId;
	
	//local ref base for 
	private HAPLocalReferenceBase m_localRefBase;
	
	private HAPProcessTracker m_processTracker;
	
	public HAPContextProcessor(HAPContextDomain domainContext, HAPLocalReferenceBase localRefBase, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_complexEntity = complexEntity;
		this.m_valueStructurePool = valueStructurePool;
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
			HAPResourceDefinition1 relatedResource = null;
			if(m_complexEntity instanceof HAPResourceDefinition1)   relatedResource = (HAPResourceDefinition1)m_complexEntity;
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId(), relatedResource);
			if(entity instanceof HAPDefinitionEntityComplex)  contextComplexEntity = (HAPDefinitionEntityComplex)entity;
		}
		
		return new HAPResultProcessAttachmentReference(entity, attachment.getAdaptor(), contextComplexEntity);
	}

	public HAPProcessTracker getProcessTracker() {    return this.m_processTracker;    }
	
	public HAPContextDomain getDomainContext() {    return this.m_domainContext;    }
	public HAPDomainDefinitionEntity getComplexDefinitionDomain() {   return this.definitionDomain;   }
	public HAPLocalReferenceBase getLocalReferenceBase() {   return this.m_localRefBase;    }
	
	public HAPDefinitionEntityComplex getComplexEntity() {    return this.m_complexEntity;    }
	
	public HAPComplexValueStructure getValueStructureComplex() {   return this.m_complexEntity==null?null:this.m_complexEntity.getValueStructureComplex();     }
	
	public HAPContainerAttachment getAttachmentContainer() {   return this.m_complexEntity==null?null:this.m_complexEntity.getAttachmentContainer();    }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }

}
