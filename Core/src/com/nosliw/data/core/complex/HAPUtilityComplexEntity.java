package com.nosliw.data.core.complex;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.data.core.complex.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.complex.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPContextDomain;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPartComplexValueStructureGroupWithEntity;
import com.nosliw.data.core.domain.entity.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.resource.HAPEntityResourceDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityComplexEntity {

	public static void traversComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorComplexEntity processor, HAPContextProcessor processContext) {
		traversComplexEntityDefinitionTree(processContext.getDomainContext().getDefinitionDomain().getComplexEntityInfo(entityId), null, processor, processContext);
	}

	private static void traversComplexEntityDefinitionTree(HAPInfoDefinitionEntityInDomainComplex complexEntityInfo, HAPInfoDefinitionEntityInDomainComplex parentComplexEntityInfo, HAPProcessorComplexEntity processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(complexEntityInfo, parentComplexEntityInfo, processContext);
		
		HAPContextDomain domainContext = processContext.getDomainContext();
		HAPDomainDefinitionEntity defDomain = domainContext.getDefinitionDomain();

		HAPDefinitionEntityComplex complexEntityDef = complexEntityInfo.getComplexEntity();
				
		//process attribute entity
		Map<String, HAPIdEntityInDomain> simpleAttributes = complexEntityDef.getSimpleAttributes();
		for(String attrName : simpleAttributes.keySet()) {
			HAPIdEntityInDomain attrEntityDefId = simpleAttributes.get(attrName);
			HAPInfoDefinitionEntityInDomain attrEntityInfo = defDomain.getEntityInfo(attrEntityDefId);
			if(attrEntityInfo.isComplexEntity()) {
				traversComplexEntityDefinitionTree((HAPInfoDefinitionEntityInDomainComplex)attrEntityInfo, complexEntityInfo, processor, new HAPContextProcessor(domainContext, attrEntityInfo.getLocalBaseReference(), processContext.getRuntimeEnvironment()));
			}
		}

		//process container attribute entity
		Map<String, HAPContainerEntity> containerAttributes = complexEntityDef.getContainerAttributes();
		for(String attrName : containerAttributes.keySet()) {
			HAPContainerEntity container = containerAttributes.get(attrName);
			List<HAPInfoContainerElement> eleInfos = container.getElements();
			for(HAPInfoContainerElement eleInfo : eleInfos) {
				HAPIdEntityInDomain eleId = eleInfo.getElementId();
				HAPInfoDefinitionEntityInDomain eleEntityInfo = defDomain.getEntityInfo(eleId);
				if(eleEntityInfo.isComplexEntity()) {
					traversComplexEntityDefinitionTree((HAPInfoDefinitionEntityInDomainComplex)eleEntityInfo, complexEntityInfo, processor, new HAPContextProcessor(domainContext, eleEntityInfo.getLocalBaseReference(), processContext.getRuntimeEnvironment()));
				}
			}
		}
	}

	public static HAPIdEntityInDomain getEntityId(HAPEntityResourceDefinition entity) {
		return new HAPIdEntityInDomain(entity.getId(), entity.getEntityType());
	}
	
	public static HAPResultSolveReference solveReference(Object refObj, String dataType, HAPContextProcessor context) {
		HAPRuntimeEnvironment runtimeEnv = context.getRuntimeEnvironment();
		
		//figure out reference is resource id or attachment reference
		HAPResourceId resourceId = null;
		HAPReferenceAttachment attachmentReference = null;
		
		if(refObj instanceof String) {
			String refStr = (String)refObj;
			resourceId = HAPUtilityResourceId.buildResourceIdByLiterate(dataType, refStr, true);
			if(resourceId==null)    attachmentReference = HAPReferenceAttachment.newInstance(refStr, dataType);
		}
		else if(refObj instanceof JSONObject) {
			JSONObject refJsonObj = (JSONObject)refObj;
			if(refJsonObj.opt(HAPReferenceAttachment.DATATYPE)!=null) {
				attachmentReference = HAPReferenceAttachment.newInstance(refJsonObj, dataType);
			}
			else {
				resourceId = HAPFactoryResourceId.newInstance(dataType, refObj);
			}
		}
		
		Object entity = null;
		HAPContextProcessor contextResult = null;
		HAPDefinitionEntityComplex contextComplexEntity = null;
		
		if(resourceId!=null) {
			//is resource id
			//reference name is resource id
			HAPResourceDefinition resourceDef = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, context.getComplexDefinitionDomain(), context.getLocalReferenceBase());
			contextResult = new HAPContextProcessor(context.getDomainContext(), resourceDef.getLocalReferenceBase(), resourceDef.getEntityId(), context.getRuntimeEnvironment());
			return HAPResultSolveReference.newResultFromResource(resourceDef.getEntityId(), contextResult);
		}
		else {
			//reference name is reference to attachment
			HAPResultProcessAttachmentReference result = context.processAttachmentReference(attachmentReference.getDataType(), attachmentReference.getName());
			HAPIdEntityInDomain entityId = runtimeEnv.getResourceDefinitionManager().parseEntityDefinition(result.getEntity(), attachmentReference.getDataType(), context.getComplexDefinitionDomain(), context.getLocalReferenceBase());
			contextResult = new HAPContextProcessor(context.getDomainContext(), context.getLocalReferenceBase(), entityId, context.getRuntimeEnvironment());
			return HAPResultSolveReference.newResultFromAttachment(entityId, (JSONObject)result.getAdaptor(), contextResult);
		}
	}
	

	public static HAPIdEntityInDomain newExecutableEntity(Class<? extends HAPExecutableEntityComplex> exeClass, HAPIdEntityInDomain definitionEntityId, HAPContextProcessor processContext) {  
		HAPIdEntityInDomain out = null;
		try {
			HAPExecutableEntityComplex complexEntityExe = HAPExecutableEntityComplex.class.newInstance();
			//exe share the same value structure complex with definition
			complexEntityExe.setValueStructureComplexId(processContext.getComplexDefinitionDomain().getEntity(definitionEntityId).getValueStructureComplexId());
			out = processContext.getDomainContext().addExecutableEntity(complexEntityExe, definitionEntityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	//merge attachment from parent to child
	public static void processAttachment(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationAttachment attachmentRelation) {
		HAPUtilityComponent.mergeWithParentAttachment(complexEntity, parentEntity.getAttachmentContainer());    //build attachment
	}
	
	public static void processValueStructureInheritance(HAPIdEntityInDomain complexId, HAPIdEntityInDomain parentComplexId, HAPDomainValueStructure valueStructureDomain) {
		
	}


	public static void processValueStructureInheritance(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationValueStructure valueStructureRelation) {
		//expand reference
		
		 //inheritance
		if(valueStructureRelation.isInheritable()) {
			HAPComplexValueStructure parentValueStructureComplex = parentEntity.getValueStructureComplex();
			List<HAPPartComplexValueStructure> parts = parentValueStructureComplex.getParts();
			
			if(valueStructureRelation.isShareRuntimeData()) {
				//share runtime data with parent
				HAPPartComplexValueStructureGroupWithEntity part = new HAPPartComplexValueStructureGroupWithEntity(HAPUtilityComplexValueStructure.createPartInfoFromParent());
				for(HAPPartComplexValueStructure parentPart : parts) {
					part.addChild(parentPart.cloneComplexValueStructurePart());
				}
				complexEntity.getValueStructureComplex().addPart(part);
			}
			else {
				//child has own data
				HAPPartComplexValueStructureGroupWithEntity part = new HAPPartComplexValueStructureGroupWithEntity(HAPUtilityComplexValueStructure.createPartInfoFromParent());
				for(HAPPartComplexValueStructure parentPart : parts) {
					part.addChild(parentPart.cloneComplexValueStructurePart());
				}
				complexEntity.getValueStructureComplex().addPart(part);
			}
		}
		
		
	}
	
	public static void processInfo(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationInfo infoRelation) {
		HAPInfoUtility.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());

	}
	
}
