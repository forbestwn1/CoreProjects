package com.nosliw.data.core.domain;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImp implements HAPPluginEntityDefinitionInDomain{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Class<? extends HAPDefinitionEntityInDomain> m_entityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityDefinitionInDomainImp(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityClass = entityClass;
		
		//get entity type from class
		this.m_entityType = HAPUtilityDomain.getEntityTypeFromEntityClass(entityClass);
	}
	
	public HAPPluginEntityDefinitionInDomainImp(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityClass = entityClass;
		this.m_entityType = entityType;
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;	}

	@Override
	public HAPIdEntityInDomain parseDefinition(Object obj, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = null;
		try {
			
			HAPDefinitionEntityInDomain entity = this.m_entityClass.newInstance();
			
			//add to domain
			out = parserContext.getDefinitionDomain().addEntity(entity, parserContext.getLocalReferenceBase());

			//parse entity content
			this.parseDefinitionContent(out, obj, parserContext.getDefinitionDomain());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	@Override
	public void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		try {
			
			HAPDefinitionEntityInDomain entity = this.m_entityClass.newInstance();
			
			//add to domain
			parserContext.getDefinitionDomain().setEntity(entityId, entity, parserContext.getLocalReferenceBase());

			//parse entity content
			this.parseDefinitionContent(entityId, obj, parserContext.getDefinitionDomain());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPDomainDefinitionEntity definitionDomain);
	

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
		return entity;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected void parseEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPDomainDefinitionEntity definitionDomain) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(attrEntityType)) {
			parseComplexEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, definitionDomain);
		}
		else {
			parseSimpleEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, definitionDomain);
		}
	}
	
	protected void parseSimpleEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPDomainDefinitionEntity definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
			HAPEmbededEntity attributeEntity =  HAPUtilityParserEntity.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.m_runtimeEnv.getDomainEntityManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}

	protected void parseComplexEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPDomainDefinitionEntity definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
			HAPEmbededEntity attributeEntity =  HAPUtilityParserEntity.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.m_runtimeEnv.getDomainEntityManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}
	
	protected void parseContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPDomainDefinitionEntity definitionDomain) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(eleEntityType)) {
			parseComplexContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, null, definitionDomain);
		}
		else {
			parseSimpleContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, definitionDomain);
		}
	}
	
	protected void parseSimpleContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPDomainDefinitionEntity definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
			List<HAPInfoContainerElement> eleInfos = HAPUtilityParserEntity.parseContainer(attrEntityObj, eleEntityType, adapterType, containerType, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.m_runtimeEnv.getDomainEntityManager());
			for(HAPInfoContainerElement eleInfo : eleInfos) {
				entity.addContainerElementAttribute(attributeName, eleInfo);
			}
		}
	}

	protected void parseComplexContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPDomainDefinitionEntity definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
			List<HAPInfoContainerElement> eleInfos = HAPUtilityParserEntity.parseComplexContainer(attrEntityObj, eleEntityType, adapterType, containerType, entityId, parentRelationConfigureDefault, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.m_runtimeEnv.getDomainEntityManager());
			for(HAPInfoContainerElement eleInfo : eleInfos) {
				entity.addContainerElementAttribute(attributeName, eleInfo);
			}
		}
	}
}
