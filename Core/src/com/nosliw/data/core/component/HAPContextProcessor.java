package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.complex.HAPManagerComplexEntity;
import com.nosliw.data.core.domain.HAPExecutableBundleComplexResource;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPInfoAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessor {

	private HAPManagerComplexEntity m_complexEntityManager;
	
	private HAPExecutableBundleComplexResource m_complexResourceBundle;
	
	//runtime
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPProcessTracker m_processTracker;
	
	public HAPContextProcessor(HAPResourceIdSimple currentResourceId, HAPDomainEntityDefinitionGlobal definitionDomain, HAPRuntimeEnvironment runtimeEnv) {
		this(new HAPExecutableBundleComplexResource(currentResourceId, definitionDomain), runtimeEnv);
	}

	public HAPContextProcessor(HAPExecutableBundleComplexResource currentResourceBundle, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_complexResourceBundle = currentResourceBundle;
		this.m_complexEntityManager = this.m_runtimeEnv.getComplexEntityManager();
	}

	public HAPResourceIdSimple getCurrentComplexResourceId() {     return this.m_complexResourceBundle.getRootResourceId();     }
	
	public HAPExecutableBundleComplexResource getCurrentComplexResourceBundle() {     return this.m_complexResourceBundle;      }
	
	public HAPDomainEntityDefinitionGlobal getCurrentDefinitionDomain() {    return this.getCurrentComplexResourceBundle().getDefinitionDomain();     }
	public HAPDomainEntityExecutableResourceComplex getCurrentExecutableDomain() {     return this.getCurrentComplexResourceBundle().getExecutableDomain();       }
	
	public HAPDomainAttachment getCurrentAttachmentDomain() {    return this.getCurrentComplexResourceBundle().getAttachmentDomain();     }
	public HAPDomainValueStructure getCurrentValueStructureDomain() {    return this.getCurrentComplexResourceBundle().getValueStructureDomain();     }
	

	
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
	
	public HAPDefinitionEntityComplexValueStructure getValueStructureComplex() {   return this.m_complexEntity==null?null:this.m_complexEntity.getValueStructureComplex();     }
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer() {   return this.m_complexEntity==null?null:this.m_complexEntity.getAttachmentContainer();    }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }

}
