package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPDomainEntityExecutable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;

public class HAPUtilityValueStructure {

	public static void buildValueStructureDomain(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		buildValueStructureComplexTree(rootComplexEntityExecutableId, processContext);
		
		
	}

	//add attachment container to attachment domain
	private static void buildValueStructureComplexTree(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Object adapter,
					HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				
				HAPContextDomain domainContext = processContext.getDomainContext();
				HAPDomainEntityDefinition defDomain = domainContext.getDefinitionDomain();
				HAPDomainEntityExecutable exeDomain = domainContext.getExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPIdEntityInDomain entityIdDef = domainContext.getDefinitionEntityIdByExecutableId(entityIdExe);
				HAPDefinitionEntityContainerAttachment attachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = defDomain.getSolidEntityInfoDefinition(entityIdDef, attachmentContainer);
				
				HAPDefinitionEntityComplexValueStructure valueStructureComplexEntity = null;
				HAPEmbededEntity valueStructureComplexAttribute = ((HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity()).getValueStructureComplexEntity();
				if(valueStructureComplexAttribute!=null) {
					valueStructureComplexEntity = (HAPDefinitionEntityComplexValueStructure)defDomain.getEntityInfoDefinition(valueStructureComplexAttribute.getEntityId()).getEntity();
				}

				String valueStructureId =  valueStructureDomain.addValueStructureComplex(valueStructureComplexEntity, defDomain, attachmentContainer);
				exeDomain.getEntityInfoExecutable(entityIdExe).getEntity().setValueStructureComplexId(valueStructureId);
				
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
