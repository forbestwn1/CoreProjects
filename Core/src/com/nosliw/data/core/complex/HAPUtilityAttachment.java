package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

public class HAPUtilityAttachment {

	public static void processAttachment(HAPIdEntityInDomain rootComplexEntityDefinitionId, HAPContextProcessor processContext) {
		buildAttachmentContainer(rootComplexEntityDefinitionId, processContext);
		mergeAttachment(rootComplexEntityDefinitionId, processContext);
		expandAttachmentReference(rootComplexEntityDefinitionId, processContext);
	}
	
	private static void expandAttachmentReference(HAPIdEntityInDomain rootComplexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traversEntityTree(rootComplexEntityDefinitionId, new HAPProcessorEntity() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition entityInfo, Object adapter,
					HAPInfoEntityInDomainDefinition parentEntityInfo, HAPContextProcessor processContext) {
				
			}
		}, processContext);
	}

	//add attachment container to attachment domain
	private static void buildAttachmentContainer(HAPIdEntityInDomain rootComplexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPUtilityComplexEntity.traversComplexEntityTree(rootComplexEntityDefinitionId, new HAPProcessorEntity() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition complexEntityDefInfo, Object adapter, HAPInfoEntityInDomainDefinition parentComplexEntityDefInfo,
					HAPContextProcessor processContext) {
				HAPContextDomain domainContext = processContext.getDomainContext();
				HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
				HAPDomainAttachment attachmentDomain = domainContext.getAttachmentDomain();

				HAPDefinitionEntityContainerAttachment attachmentContainerEntity = null;
				HAPEmbededEntity attachmentContainerAttribute = ((HAPDefinitionEntityInDomainComplex)complexEntityDefInfo.getEntity()).getAttachmentContainerEntity();
				if(attachmentContainerAttribute!=null) {
					attachmentContainerEntity = (HAPDefinitionEntityContainerAttachment)defDomain.getEntityInfoDefinition(attachmentContainerAttribute.getEntityId()).getEntity();
				}
				attachmentDomain.addAttachmentContainer(attachmentContainerEntity, complexEntityDefInfo.getEntityId());
			}}, processContext);
	}

	//merge attachment between paren and child
	private static void mergeAttachment(HAPIdEntityInDomain rootComplexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPUtilityComplexEntity.traversComplexEntityTree(rootComplexEntityDefinitionId, new HAPProcessorEntity() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition complexEntityDefInfo, Object adapter, HAPInfoEntityInDomainDefinition parentComplexEntityDefInfo,
					HAPContextProcessor processContext) {
				if(parentComplexEntityDefInfo!=null) {
					HAPContextDomain domainContext = processContext.getDomainContext();
					HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
					HAPDomainAttachment attachmentDomain = domainContext.getAttachmentDomain();

					HAPDefinitionEntityContainerAttachment childAttachmentContainer =  attachmentDomain.getAttachmentContainer(complexEntityDefInfo.getEntityId());
					
					HAPConfigureComplexRelationAttachment attachmentParentRelation = null;
					HAPInfoParentComplex parentInfo = defDomain.getParentInfo(complexEntityDefInfo.getEntityId());
					if(parentInfo!=null) {
						HAPConfigureParentRelationComplex parentRelation = parentInfo.getParentRelationConfigure();
						attachmentParentRelation = parentRelation.getAttachmentRelationMode();
					}
					HAPDefinitionEntityContainerAttachment parentAttachmentContainer =  attachmentDomain.getAttachmentContainer(parentComplexEntityDefInfo.getEntityId());
					childAttachmentContainer.merge(parentAttachmentContainer, attachmentParentRelation);
				}

			}}, processContext);
	}
}
