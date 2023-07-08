package com.nosliw.data.core.domain.complexentity;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityInfo;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.attachment.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.domain.entity.HAPConfigureComplexRelationInfo;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPResultSolveReference;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPExecutablePartValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPExecutablePartValueContextGroupWithEntity;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.resource.HAPEntityResourceDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityComplexEntity {

	public static void traversComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityDefinition processor, HAPContextProcessor processContext) {
		HAPUtilityDomain.traversDefinitionEntityTree(entityId, new HAPProcessorEntityDefinition() {
			@Override
			public void process(HAPInfoEntityInDomainDefinition entityInfo, Object adapter,	HAPInfoEntityInDomainDefinition parentEntityInfo, HAPContextProcessor processContext) {
				if(entityInfo.isComplexEntity()) {
					processor.process(entityInfo, adapter, parentEntityInfo, processContext);
				}
			}
		}, processContext);
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
		HAPDefinitionEntityInDomainComplex contextComplexEntity = null;
		
		if(resourceId!=null) {
			//is resource id
			//reference name is resource id
			HAPResourceDefinition resourceDef = runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, context.getCurrentDefinitionDomain(), context.getcu);
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
			complexEntityExe.setValueContextEntity(processContext.getComplexDefinitionDomain().getEntity(definitionEntityId).getValueContextEntity());
			out = processContext.getDomainContext().addExecutableEntity(complexEntityExe, definitionEntityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	//merge attachment from parent to child
	public static void processAttachment(HAPDefinitionEntityInDomainComplex complexEntity, HAPDefinitionEntityInDomainComplex parentEntity, HAPConfigureComplexRelationAttachment attachmentRelation) {
		HAPUtilityComponent.mergeWithParentAttachment(complexEntity, parentEntity.getAttachmentContainer());    //build attachment
	}
	
	public static void processValueStructureInheritance(HAPIdEntityInDomain complexId, HAPIdEntityInDomain parentComplexId, HAPDomainValueStructure valueStructureDomain) {
		
	}


	public static void processValueStructureInheritance(HAPDefinitionEntityInDomainComplex complexEntity, HAPDefinitionEntityInDomainComplex parentEntity, HAPConfigureComplexRelationValueStructure valueStructureRelation) {
		//expand reference
		
		 //inheritance
		if(valueStructureRelation.isInheritable()) {
			HAPDefinitionEntityValueContext parentValueStructureComplex = parentEntity.getValueContext();
			List<HAPExecutablePartValueContext> parts = parentValueStructureComplex.getValueStructures();
			
			if(valueStructureRelation.isShareRuntimeData()) {
				//share runtime data with parent
				HAPExecutablePartValueContextGroupWithEntity part = new HAPExecutablePartValueContextGroupWithEntity(HAPUtilityValueContext.createPartInfoFromParent());
				for(HAPExecutablePartValueContext parentPart : parts) {
					part.addChild(parentPart.cloneValueContextPart());
				}
				complexEntity.getValueContext().addValueStructure(part);
			}
			else {
				//child has own data
				HAPExecutablePartValueContextGroupWithEntity part = new HAPExecutablePartValueContextGroupWithEntity(HAPUtilityValueContext.createPartInfoFromParent());
				for(HAPExecutablePartValueContext parentPart : parts) {
					part.addChild(parentPart.cloneValueContextPart());
				}
				complexEntity.getValueContext().addValueStructure(part);
			}
		}
		
		
	}
	
	public static void processInfo(HAPDefinitionEntityInDomainComplex complexEntity, HAPDefinitionEntityInDomainComplex parentEntity, HAPConfigureComplexRelationInfo infoRelation) {
		HAPUtilityInfo.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());

	}
	
}
