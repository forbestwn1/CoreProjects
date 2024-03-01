package com.nosliw.data.core.domain.attachment;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableDownwardImpAttribute;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPUtilityAttachment {

	//build attachment domain
	public static void buildAttachmentDomain(HAPExecutableEntityComplex rootComplexEntityExecutable, HAPContextProcessor processContext) {
		buildAttachmentTree(rootComplexEntityExecutable, processContext);
		mergeAttachment(rootComplexEntityExecutable, processContext);
	}
	
	public static HAPDefinitionEntityContainerAttachment getAttachmentContainerByComplexExe(HAPExecutableEntityComplex complexEntityExe, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
		HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
		HAPDefinitionEntityContainerAttachment attachmentContainer = attachmentDomain.getAttachmentContainer(complexEntityExe.getAttachmentContainerId());
		return attachmentContainer;
	}

	public static HAPDefinitionEntityContainerAttachment getAttachmentContainerByComplexEntityExe(HAPExecutableEntityComplex compleEntityExe, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
		HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
		HAPDefinitionEntityContainerAttachment attachmentContainer = attachmentDomain.getAttachmentContainer(compleEntityExe.getAttachmentContainerId());
		return attachmentContainer;
	}

	//add attachment container to attachment domain
	private static void buildAttachmentTree(HAPExecutableEntityComplex rootComplexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutable, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			
			private void process(HAPExecutableEntity entityExe) {
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
				
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();
				HAPInfoEntityInDomainDefinition complexEntityDefInfo = definitionGlobalDomain.getEntityInfoDefinition(entityIdDef);
				
				HAPDefinitionEntityContainerAttachment attachmentContainerEntity = null;
				HAPIdEntityInDomain attachmentContainerEntityId = ((HAPManualEntityComplex)complexEntityDefInfo.getEntity()).getAttachmentContainerEntity();
				if(attachmentContainerEntityId!=null) {
					attachmentContainerEntity = (HAPDefinitionEntityContainerAttachment)definitionGlobalDomain.getEntityInfoDefinition(attachmentContainerEntityId).getEntity();
				}
				String attachmentContainerId = attachmentDomain.addAttachmentContainer(attachmentContainerEntity);
				HAPExecutableEntityComplex complexEntityExe = (HAPExecutableEntityComplex)entityExe;
				complexEntityExe.setAttachmentContainerId(attachmentContainerId);
			}
			
			@Override
			public void processRootEntity(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {
				process(entityExe);
			}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;
				process(parentComplexEntity.getComplexEntityAttributeValue(attribute));
				return true;
			}
		}, processContext);
	}

	//merge attachment between paren and child
	private static void mergeAttachment(HAPExecutableEntityComplex rootComplexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutable, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity entityExe, HAPContextProcessor processContext) {	}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPExecutableBundle complexEntityPackage = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPDefinitionEntityContainerAttachment childAttachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExe(entityExe, processContext);
				
				HAPConfigureComplexRelationAttachment attachmentParentRelation = null;
				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				if(parentInfo!=null) {
					HAPConfigureParentRelationComplex parentRelation = parentInfo.getParentRelationConfigure();
					attachmentParentRelation = parentRelation.getAttachmentRelationMode();
				}
				HAPDefinitionEntityContainerAttachment parentAttachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexEntityExe(parentComplexEntity, processContext);
				childAttachmentContainer.merge(parentAttachmentContainer, attachmentParentRelation);
				return true;
			}
		}, processContext);
	}
}
