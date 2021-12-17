package com.nosliw.data.core.domain;

import java.lang.reflect.Field;

import org.json.JSONObject;

public abstract class HAPPluginEntityDefinitionInDomainImp implements HAPPluginEntityDefinitionInDomain{

	private Class<? extends HAPDefinitionEntityInDomain> m_entityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityDefinitionInDomainImp(Class<? extends HAPDefinitionEntityInDomain> entityClass) {
		this.m_entityClass = entityClass;
		
		//get entity type from class
		try {
			Field f = this.m_entityClass.getField("ENTITY_TYPE");
			this.m_entityType = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;	}

	@Override
	public HAPIdEntityInDomain parseDefinition(JSONObject jsonObj, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = null;
		try {
			
			HAPDefinitionEntityInDomain entity = this.m_entityClass.newInstance();
			
			//add to domain
			out = parserContext.getDefinitionDomain().addEntity(entity, parserContext.getLocalReferenceBase());

			//parse entity content
			this.parseDefinitionContent(out, jsonObj, parserContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomain entity = parserContext.getDefinitionDomain().getEntityInfo(entityId).getEntity();
		return entity;
	}
	
	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext);
}
