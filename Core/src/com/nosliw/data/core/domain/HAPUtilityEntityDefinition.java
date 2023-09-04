package com.nosliw.data.core.domain;

import java.lang.reflect.Field;
import java.util.List;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityDefinition;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpression;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityEntityDefinition {

	public static void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP, parserContext, runtimeEnv);
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP, parserContext, runtimeEnv);
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP, parserContext, runtimeEnv);
	}

	
	public static String addPlainScriptExpressionToComplexEntity(HAPDefinitionExpression expression, HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomainComplex complexEntity = (HAPDefinitionEntityInDomainComplex)parserContext.getGlobalDomain().getEntityInfoDefinition(complexEntityId).getEntity();
		return complexEntity.getPlainScriptExpressionGroupEntity(parserContext).addExpression(expression);
	}
	
	public static HAPIdEntityInDomain newTransparentAttribute(HAPIdEntityInDomain parentEntityId, String attrEntityType, String attrName, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionEntityInDomain entity = parserContext.getGlobalDomain().getEntityInfoDefinition(parentEntityId).getEntity();
		HAPIdEntityInDomain attrEntityId = runtimeEnv.getDomainEntityDefinitionManager().newDefinitionInstance(attrEntityType, parserContext);
		
		HAPConfigureParentRelationComplex parentRelationConfigure = new HAPConfigureParentRelationComplex();
		parentRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_RUNTIME);
		
		HAPUtilityEntityDefinition.buildParentRelation(attrEntityId, parentEntityId, parentRelationConfigure, parserContext);
		entity.setAttributeValueComplex(attrName, attrEntityId);
		return attrEntityId;
	}

	public static HAPIdEntityInDomain newAttribute(HAPIdEntityInDomain parentEntityId, String attrEntityType, String attrName, HAPConfigureParentRelationComplex relationConfigure, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionEntityInDomain entity = parserContext.getGlobalDomain().getEntityInfoDefinition(parentEntityId).getEntity();
		HAPIdEntityInDomain attrEntityId = runtimeEnv.getDomainEntityDefinitionManager().newDefinitionInstance(attrEntityType, parserContext);
		HAPUtilityEntityDefinition.buildParentRelation(attrEntityId, parentEntityId, relationConfigure, parserContext);
		entity.setAttributeValueComplex(attrName, attrEntityId);
		return attrEntityId;
	}
	
	public static void buildParentRelation(HAPIdEntityInDomain childEntityId, HAPIdEntityInDomain parentEntityId, String valueContextInheritMode, HAPContextParser parserContext) {
		HAPConfigureParentRelationComplex parentRelationConfigure = new HAPConfigureParentRelationComplex();
		parentRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(valueContextInheritMode);
		buildParentRelation(childEntityId, parentEntityId, parentRelationConfigure, parserContext);
	}

	public static void buildParentRelation(HAPIdEntityInDomain childEntityId, HAPIdEntityInDomain parentEntityId, HAPConfigureParentRelationComplex relationConfigure, HAPContextParser parserContext) {
		HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();
		parentInfo.setParentId(parentEntityId);
		parentInfo.setParentRelationConfigure(relationConfigure);
		((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(childEntityId, parentInfo);
	}

	public static void traverseDefinitionComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityDefinition processor, HAPDomainEntityDefinitionGlobal definitionDomain, Object globalObj) {
		processor.processComplexRoot(entityId, globalObj);
		traverseDefinitionEntityTreeLeaf(entityId, processor, definitionDomain, globalObj);
	}

	private static void traverseDefinitionEntityTreeLeaf(HAPIdEntityInDomain parentEntityId, HAPProcessorEntityDefinition processor, HAPDomainEntityDefinitionGlobal definitionDomain, Object globalObj) {
		//process current entity
		HAPInfoEntityInDomainDefinition parentEntityInfo = definitionDomain.getEntityInfoDefinition(parentEntityId);
		HAPDefinitionEntityInDomain complexEntity = parentEntityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityDefinition> attrsDef = complexEntity.getAttributes();
			for(HAPAttributeEntityDefinition attrDef : attrsDef) {
				processor.processAttribute(parentEntityId, attrDef.getName(), globalObj);
				
				if(attrDef.getValueTypeInfo().getIsComplex()) {
					HAPEmbededDefinition embededDef = attrDef.getValue();
					HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)embededDef.getValue();
					traverseDefinitionEntityTreeLeaf(attrEntityId, processor, definitionDomain, globalObj);
				}
			}
		}
	}

	//solid entity means:
	//   if entity is attachment ref, then replace with real entity
	//   if entity is local or global simple resource ref, then replace with real entity
	public static void solidateLocalResourceReference(HAPIdEntityInDomain rootEntityDefinitionId, HAPDomainEntityDefinitionGlobal definitionDomain, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPUtilityEntityDefinition.traverseDefinitionComplexEntityTree(
			rootEntityDefinitionId, 
			new HAPProcessorEntityDefinition() {
				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, Object globalObj) {	}

				@Override
				public void processAttribute(HAPIdEntityInDomain parentEntityId, String attrName, Object globalObj) {
					HAPInfoEntityInDomainDefinition entityInfo = definitionDomain.getEntityInfoDefinition(parentEntityId);
					HAPDefinitionEntityInDomain parentEntity = entityInfo.getEntity();
					HAPAttributeEntityDefinition attrDef = parentEntity.getAttribute(attrName);
					HAPEmbededDefinition embededDef = attrDef.getValue();
					Object entityDef = embededDef.getValue();
					if(entityDef instanceof HAPIdEntityInDomain) {
						HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)entityDef;
						HAPInfoEntityInDomainDefinition attrEntityInfo = definitionDomain.getEntityInfoDefinition(attrEntityId);

						if(attrEntityInfo.getReferedResourceId()!=null&&attrEntityInfo.getEntity()==null){
							if(attrEntityInfo.isLocalResourceReference() || attrEntityInfo.isGlobalSimpleResourceReference()) {
								HAPIdEntityInDomain resourceEntityId = resourceDefinitionManager.getResourceDefinition(attrEntityInfo.getReferedResourceId(), definitionDomain, attrEntityId.getDomainId()).getEntityId();
								HAPInfoEntityInDomainDefinition resourceEntityInfo = definitionDomain.getEntityInfoDefinition(resourceEntityId);
								HAPUtilityEntityInfo.softMerge(attrEntityInfo.getExtraInfo(), resourceEntityInfo.getExtraInfo());
								attrEntityInfo.setEntity(resourceEntityInfo.getEntity());
								
								//solidate resource entity
								solidateLocalResourceReference(attrEntityId, definitionDomain, resourceDefinitionManager);
							}
						}
					}
				}
			}, definitionDomain, null);
	}
	
	//solid entity means:
	//   if entity is attachment ref, then replace with real entity
	//   if entity is local or global simple resource ref, then replace with real entity
/*
	public HAPInfoEntityInDomainDefinition getSolidEntityInfoDefinition(HAPIdEntityInDomain entityId, HAPDefinitionEntityContainerAttachment attachmentContainer) {
		HAPInfoEntityInDomainDefinition out = this.getEntityInfoDefinition(entityId);
		if(out.getEntity()==null){
			if(out.getReferedResourceId()!=null) {
				if(out.isLocalResourceReference() || out.isGlobalSimpleResourceReference()) {
					HAPIdEntityInDomain resourceEntityId = this.m_resourceDefinitionManager.getResourceDefinition(out.getReferedResourceId(), this, entityId.getDomainId()).getEntityId();
					HAPInfoEntityInDomainDefinition entityInfo = this.getEntityInfoDefinition(resourceEntityId);
					HAPUtilityEntityInfo.softMerge(out.getExtraInfo(), entityInfo.getExtraInfo());
					out.setEntity(entityInfo.getEntity());
				}
			}
			else if(out.getAttachmentReference()!=null) {
				HAPAttachmentEntity attachment = (HAPAttachmentEntity)attachmentContainer.getElement(out.getAttachmentReference());
				Object entityObj = attachment.getEntity();
				out.setEntity(this.getEntityInfoDefinition(HAPUtilityParserEntity.parseEntity(entityObj, attachment.getValueType(), new HAPContextParser(this, entityId.getDomainId()), this.m_entityDefManager, null)).getEntity());
			}
		}
		return out;
	}
*/
	
	
	
	public static HAPEmbededDefinitionWithId newEmbededDefinitionWithId(HAPIdEntityInDomain entityId, HAPIdEntityInDomain adapterEntityId, HAPManagerDomainEntityDefinition entityDefDomainMan) {
		return new HAPEmbededDefinition(entityId, adapterEntityId);
	}
	
	public static HAPEmbededDefinitionWithId setEntitySimpleAttributeWithId(HAPDefinitionEntityInDomain entityDef, String attributeName, HAPIdEntityInDomain attrEntityId, HAPManagerDomainEntityDefinition entityDefDomainMan) {
		HAPEmbededDefinitionWithId embeded = new HAPEmbededDefinitionWithId(attrEntityId, entityDefDomainMan.isComplexEntity(attrEntityId.getEntityType()));
		entityDef.setNormalAttribute(attributeName, embeded);
		return embeded;
	}
	
	//get entity type from class
	public static String getEntityTypeFromEntityClass(Class<? extends HAPDefinitionEntityInDomain> entityClass) {
		String out = null;
		try {
			Field f = entityClass.getField("ENTITY_TYPE");
			out = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static HAPInfoEntityInDomainDefinition newEntityDefinitionInfoInDomain(String entityType, HAPManagerDomainEntityDefinition entityDefMan) {
		HAPInfoEntityInDomainDefinition out = new HAPInfoEntityInDomainDefinition(entityType);
		out.setIsComplexEntity(entityDefMan.isComplexEntity(entityType));
		return out;
	}

	public static HAPIdEntityInDomain getEntityDescent(HAPIdEntityInDomain entityId, HAPPath p, HAPDomainEntityDefinitionGlobal globalDomain) {
		if(p==null || p.isEmpty())  return entityId;
		HAPIdEntityInDomain currentEntityId = entityId;
		HAPInfoEntityInDomainDefinition currentEntityInfo = globalDomain.getEntityInfoDefinition(currentEntityId);
		HAPDefinitionEntityInDomain currentEntityDef = globalDomain.getEntityInfoDefinition(currentEntityId).getEntity();
		for(String seg : p.getPathSegments()) {
			currentEntityId = currentEntityDef.getChild(seg);
			currentEntityDef = globalDomain.getEntityInfoDefinition(currentEntityId).getEntity();
		}
		return currentEntityId;
	}

}
