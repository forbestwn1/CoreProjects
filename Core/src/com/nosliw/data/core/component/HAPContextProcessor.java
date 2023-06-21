package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPManagerDomainEntityExecutable;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment1.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment1.HAPInfoAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessor {

	private HAPManagerDomainEntityExecutable m_complexEntityManager;
	
	private HAPExecutableBundle m_bundle;
	
	//runtime
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPProcessTracker m_processTracker;
	
	public HAPContextProcessor(HAPResourceIdSimple currentResourceId, HAPDomainEntityDefinitionGlobal definitionDomain, HAPRuntimeEnvironment runtimeEnv) {
		this(new HAPExecutableBundle(currentResourceId, definitionDomain), runtimeEnv);
	}

	public HAPContextProcessor(HAPExecutableBundle currentBundle, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_bundle = currentBundle;
		this.m_complexEntityManager = this.m_runtimeEnv.getDomainEntityExecutableManager();
		this.m_processTracker = new HAPProcessTracker();
	}

	public HAPResourceIdSimple getCurrentComplexResourceId() {     return this.m_bundle.getRootResourceId();     }
	
	public HAPExecutableBundle getCurrentBundle() {     return this.m_bundle;      }
	
	public HAPDomainEntityDefinitionGlobal getCurrentDefinitionDomain() {    return this.getCurrentBundle().getDefinitionDomain();     }
	public HAPDomainEntityExecutableResourceComplex getCurrentExecutableDomain() {     return this.getCurrentBundle().getExecutableDomain();       }
	
	public HAPDomainAttachment getCurrentAttachmentDomain() {    return this.getCurrentBundle().getAttachmentDomain();     }
	public HAPDomainValueStructure getCurrentValueStructureDomain() {    return this.getCurrentBundle().getValueStructureDomain();     }
	

	
	public HAPResultProcessAttachmentReference processAttachmentReference(String attachmentValueType, String attachmentName) {
		HAPAttachment attachment = this.m_complexEntity.getAttachment(attachmentValueType, attachmentName);
		String attType = attachment.getType();
		Object entity = null;
		HAPDefinitionEntityInDomainComplex contextComplexEntity = null;
		if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)) {
			entity = this.m_runtimeEnv.getAttachmentManager().parseEntityAttachment(new HAPInfoAttachment(attachmentValueType, attachmentName, ((HAPAttachmentEntity)attachment).getEntity()), this.m_complexEntity);
			contextComplexEntity = this.m_complexEntity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL)) {
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getLocalResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId());
			if(entity instanceof HAPDefinitionEntityInDomainComplex)  contextComplexEntity = (HAPDefinitionEntityInDomainComplex)entity;
		}
		else if(attType.equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)) {
			HAPResourceDefinition1 relatedResource = null;
			if(m_complexEntity instanceof HAPResourceDefinition1)   relatedResource = (HAPResourceDefinition1)m_complexEntity;
			entity = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(((HAPAttachmentReference)attachment).getReferenceId(), relatedResource);
			if(entity instanceof HAPDefinitionEntityInDomainComplex)  contextComplexEntity = (HAPDefinitionEntityInDomainComplex)entity;
		}
		
		return new HAPResultProcessAttachmentReference(entity, attachment.getAdaptor(), contextComplexEntity);
	}

	public HAPProcessTracker getProcessTracker() {    return this.m_processTracker;    }
	
	public HAPDefinitionEntityValueContext getValueStructureComplex() {   return this.m_complexEntity==null?null:this.m_complexEntity.getValueContext();     }
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer() {   return this.m_complexEntity==null?null:this.m_complexEntity.getAttachmentContainer();    }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }

}
