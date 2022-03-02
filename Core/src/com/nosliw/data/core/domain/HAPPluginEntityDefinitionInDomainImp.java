package com.nosliw.data.core.domain;

import java.lang.reflect.Field;

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

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfo(entityId).getEntity();
		return entity;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPDomainDefinitionEntity definitionDomain);
}
