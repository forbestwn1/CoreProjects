package com.nosliw.data.core.domain;

import java.lang.reflect.Field;

import org.json.JSONObject;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImp implements HAPPluginEntityDefinitionInDomain{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Class<? extends HAPDefinitionEntityInDomain> m_entityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityDefinitionInDomainImp(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
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
			entity.buildEntityInfoByJson(jsonObj);
			
			//add to domain
			out = parserContext.getDefinitionDomain().addEntity(entity, parserContext.getLocalReferenceBase());

			//parse entity content
			this.parseDefinitionContent(out, jsonObj, parserContext.getDefinitionDomain());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
		return entity;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPDomainDefinitionEntity definitionDomain);
}
