package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;

public class HAPUtilityAttachment {
	public static HAPDefinitionEntityInDomain getEntity(HAPReferenceAttachment attaReference, HAPIdEntityInDomain complexeEntityId, HAPDomainAttachment attachmentDoamin) {
		HAPDefinitionEntityContainerAttachment attachmentContainer = attachmentDoamin.getAttachmentContainer(complexeEntityId);
		HAPAttachment attachment = attachmentContainer.getElement(attaReference);
	}
	
	//build attachment domain
	public static void buildAttachmentDomain(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		buildAttachmentTree(rootComplexEntityExecutableId, processContext);
		mergeAttachment(rootComplexEntityExecutableId, processContext);
	}
	
	//add attachment container to attachment domain
	private static void buildAttachmentTree(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Object adapter,
					HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				
				HAPContextDomain domainContext = processContext.getDomainContext();
				HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
				HAPDomainAttachment attachmentDomain = domainContext.getAttachmentDomain();

				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPIdEntityInDomain entityIdDef = domainContext.getDefinitionEntityIdByExecutableId(entityIdExe);
				HAPInfoEntityInDomainDefinition complexEntityDefInfo = defDomain.getSolidEntityInfoDefinition(entityIdDef);
				
				HAPDefinitionEntityContainerAttachment attachmentContainerEntity = null;
				HAPEmbededEntity attachmentContainerAttribute = ((HAPDefinitionEntityInDomainComplex)complexEntityDefInfo.getEntity()).getAttachmentContainerEntity();
				if(attachmentContainerAttribute!=null) {
					attachmentContainerEntity = (HAPDefinitionEntityContainerAttachment)defDomain.getEntityInfoDefinition(attachmentContainerAttribute.getEntityId()).getEntity();
				}
				attachmentDomain.addAttachmentContainer(attachmentContainerEntity, entityIdExe);

			}}, processContext);
	}

	//merge attachment between paren and child
	private static void mergeAttachment(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityExeInfo, Object adapter, HAPInfoEntityInDomainExecutable parentComplexEntityExeInfo,
					HAPContextProcessor processContext) {
				if(parentComplexEntityExeInfo!=null) {
					HAPContextDomain domainContext = processContext.getDomainContext();
					HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
					HAPDomainAttachment attachmentDomain = domainContext.getAttachmentDomain();

					HAPIdEntityInDomain entityIdExe = entityExeInfo.getEntityId();
					HAPIdEntityInDomain entityIdDef = domainContext.getDefinitionEntityIdByExecutableId(entityIdExe);

					HAPDefinitionEntityContainerAttachment childAttachmentContainer =  attachmentDomain.getAttachmentContainer(entityIdExe);
					
					HAPConfigureComplexRelationAttachment attachmentParentRelation = null;
					HAPInfoParentComplex parentInfo = defDomain.getParentInfo(entityIdDef);
					if(parentInfo!=null) {
						HAPConfigureParentRelationComplex parentRelation = parentInfo.getParentRelationConfigure();
						attachmentParentRelation = parentRelation.getAttachmentRelationMode();
					}
					HAPDefinitionEntityContainerAttachment parentAttachmentContainer =  attachmentDomain.getAttachmentContainer(parentComplexEntityExeInfo.getEntityId());
					childAttachmentContainer.merge(parentAttachmentContainer, attachmentParentRelation);
				}

			}}, processContext);
	}
}
