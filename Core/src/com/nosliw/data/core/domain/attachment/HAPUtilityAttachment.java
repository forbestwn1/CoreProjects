package com.nosliw.data.core.domain.attachment;

import java.util.Set;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainAttachment;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable1;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

public class HAPUtilityAttachment {

	//build attachment domain
	public static void buildAttachmentDomain(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		buildAttachmentTree(rootComplexEntityExecutableId, processContext);
		mergeAttachment(rootComplexEntityExecutableId, processContext);
	}
	
	public static HAPDefinitionEntityContainerAttachment getAttachmentContainerByComplexExeId(HAPIdEntityInDomain entityIdExe, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
		HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
		HAPDefinitionEntityContainerAttachment attachmentContainer = attachmentDomain.getAttachmentContainer(exeDomain.getEntityInfoExecutable(entityIdExe).getEntity().getAttachmentContainerId());
		return attachmentContainer;
	}

	public static HAPDefinitionEntityContainerAttachment getAttachmentContainerByComplexEntityExe(HAPExecutableEntityComplex compleEntityExe, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
		HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
		HAPDefinitionEntityContainerAttachment attachmentContainer = attachmentDomain.getAttachmentContainer(compleEntityExe.getAttachmentContainerId());
		return attachmentContainer;
	}

	//add attachment container to attachment domain
	private static void buildAttachmentTree(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			
			private void process(HAPIdEntityInDomain entityIdExe) {
				HAPExecutableBundle complexEntityPackage = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
				
				HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPInfoEntityInDomainDefinition complexEntityDefInfo = definitionGlobalDomain.getEntityInfoDefinition(entityIdDef);
				
				HAPDefinitionEntityContainerAttachment attachmentContainerEntity = null;
				HAPIdEntityInDomain attachmentContainerEntityId = ((HAPDefinitionEntityInDomainComplex)complexEntityDefInfo.getEntity()).getAttachmentContainerEntity();
				if(attachmentContainerEntityId!=null) {
					attachmentContainerEntity = (HAPDefinitionEntityContainerAttachment)definitionGlobalDomain.getEntityInfoDefinition(attachmentContainerEntityId).getEntity();
				}
				String attachmentContainerId = attachmentDomain.addAttachmentContainer(attachmentContainerEntity);
				exeDomain.getEntityInfoExecutable(entityIdExe).getEntity().setAttachmentContainerId(attachmentContainerId);
			}
			
			@Override
			public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
				process(entityId);
			}

			@Override
			public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;
				process(parentComplexEntity.getComplexEntityAttributeValue(attribute));
				return true;
			}
		}, processContext);
	}

	//merge attachment between paren and child
	private static void mergeAttachment(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable() {
			@Override
			public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {	}

			@Override
			public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPExecutableBundle complexEntityPackage = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();

				HAPIdEntityInDomain entityIdExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);

				HAPDefinitionEntityContainerAttachment childAttachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);
				
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
	
	//add attachment container to attachment domain
	private static void buildAttachmentTree1(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable1() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				
				HAPExecutableBundle complexEntityPackage = processContext.getCurrentBundle();
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainAttachment attachmentDomain = processContext.getCurrentAttachmentDomain();
				
				HAPIdEntityInDomain entityIdExe = entityInfo.getEntityId();
				HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);
				HAPInfoEntityInDomainDefinition complexEntityDefInfo = definitionGlobalDomain.getSolidEntityInfoDefinition(entityIdDef, null);
				
				HAPDefinitionEntityContainerAttachment attachmentContainerEntity = null;
				HAPIdEntityInDomain attachmentContainerEntityId = ((HAPDefinitionEntityInDomainComplex)complexEntityDefInfo.getEntity()).getAttachmentContainerEntity();
				if(attachmentContainerEntityId!=null) {
					attachmentContainerEntity = (HAPDefinitionEntityContainerAttachment)definitionGlobalDomain.getEntityInfoDefinition(attachmentContainerEntityId).getEntity();
				}
				String attachmentContainerId = attachmentDomain.addAttachmentContainer(attachmentContainerEntity);
				exeDomain.getEntityInfoExecutable(entityIdExe).getEntity().setAttachmentContainerId(attachmentContainerId);
			}}, processContext);
	}

	//merge attachment between paren and child
	private static void mergeAttachment1(HAPIdEntityInDomain rootComplexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityDomain.traverseExecutableComplexEntityTreeSolidOnly(rootComplexEntityExecutableId, new HAPProcessorEntityExecutable1() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityExeInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentComplexEntityExeInfo, HAPContextProcessor processContext) {
				if(parentComplexEntityExeInfo!=null) {
					HAPExecutableBundle complexEntityPackage = processContext.getCurrentBundle();
					HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();

					HAPIdEntityInDomain entityIdExe = entityExeInfo.getEntityId();
					HAPIdEntityInDomain entityIdDef = complexEntityPackage.getDefinitionEntityIdByExecutableEntityId(entityIdExe);

					HAPDefinitionEntityContainerAttachment childAttachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(entityIdExe, processContext);
					
					HAPConfigureComplexRelationAttachment attachmentParentRelation = null;
					HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
					if(parentInfo!=null) {
						HAPConfigureParentRelationComplex parentRelation = parentInfo.getParentRelationConfigure();
						attachmentParentRelation = parentRelation.getAttachmentRelationMode();
					}
					HAPDefinitionEntityContainerAttachment parentAttachmentContainer = HAPUtilityAttachment.getAttachmentContainerByComplexExeId(parentComplexEntityExeInfo.getEntityId(), processContext);
					childAttachmentContainer.merge(parentAttachmentContainer, attachmentParentRelation);
				}

			}}, processContext);
	}
}
