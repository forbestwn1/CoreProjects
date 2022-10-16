package com.nosliw.data.core.domain;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.container.HAPContainerEntity;
import com.nosliw.data.core.domain.container.HAPInfoContainerElement;
import com.nosliw.data.core.domain.container.HAPInfoContainerElementListDefinition;
import com.nosliw.data.core.domain.container.HAPInfoContainerElementSetDefinition;
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
			out = parserContext.getCurrentDomain().addEntity(entity);

			//parse entity content
			this.parseDefinitionContent(out, obj, parserContext);
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
			parserContext.getCurrentDomain().setEntity(entityId, entity);

			//parse entity content
			this.parseDefinitionContent(entityId, obj, parserContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);
	
	protected JSONObject convertToJsonObject(Object obj) {
		JSONObject out = null;
		if(obj instanceof JSONObject) out = (JSONObject)obj;
		else if(obj instanceof String)  out = new JSONObject(HAPJsonUtility.formatJson((String)obj));
		return out;
	}

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected void parseEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(attrEntityType)) {
			parseComplexEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, parserContext);
		}
		else {
			parseSimpleEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}
	
	protected void parseSimpleEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededWithId attributeEntity =  HAPUtilityParserEntity.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}

	protected void parseComplexEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededWithId attributeEntity =  HAPUtilityParserEntity.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}
	
	protected void parseContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(eleEntityType)) {
			parseComplexContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, null, parserContext);
		}
		else {
			parseSimpleContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, parserContext);
		}
	}
	
	protected void parseComplexContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			
			HAPConfigureParentRelationComplex parentRelationConfigureCustomer = null;
			boolean isEnable = true;
			HAPEntityInfo extraInfo = null;
			JSONArray eleArrayObj = null;
			if(containerObj instanceof JSONObject) {
				JSONObject containerJsonObj = (JSONObject)containerObj;
				JSONObject parentRelationConfigureObjCustomer = containerJsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.PARENT);
				if(parentRelationConfigureObjCustomer!=null) {
					parentRelationConfigureCustomer = new HAPConfigureParentRelationComplex();
					parentRelationConfigureCustomer.buildObject(parentRelationConfigureObjCustomer, HAPSerializationFormat.JSON);
				}
				eleArrayObj = containerJsonObj.getJSONArray(HAPContainerEntity.ELEMENT);
				extraInfo = HAPUtilityEntityInfo.buildEntityInfoFromJson(containerJsonObj.optJSONObject(HAPContainerEntity.EXTRA));
				isEnable = HAPUtilityEntityInfo.isEnabled(extraInfo);
			}
			else if(containerObj instanceof JSONArray) {
				eleArrayObj = (JSONArray)containerObj;
			}

			if(isEnable) {
				HAPContainerEntity entityContainer = HAPUtilityDomain.buildDefinitionContainer(containerType, eleEntityType);
				entityContainer.setExtraInfo(extraInfo);
				for(int i=0; i<eleArrayObj.length(); i++) {
					JSONObject eleObj = eleArrayObj.getJSONObject(i);
					
					//element entity
					HAPEmbededWithId embededEntity = HAPUtilityParserEntity.parseEmbededComplexEntity(eleObj, eleEntityType, adapterType, entityId, parentRelationConfigureCustomer, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
					
					//element info
					entityContainer.addEntityElement(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext));
				}
			}
		}
	}
	
	protected void parseSimpleContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			boolean isEnable = true;
			HAPEntityInfo extraInfo = null;
			JSONArray eleArrayObj = null;
			if(containerObj instanceof JSONArray)  eleArrayObj = (JSONArray)containerObj;
			else if(containerObj instanceof JSONObject) {
				JSONObject containerJsonObj = (JSONObject)containerObj;
				eleArrayObj = containerJsonObj.getJSONArray(HAPContainerEntity.ELEMENT);
				extraInfo = HAPUtilityEntityInfo.buildEntityInfoFromJson(containerJsonObj.optJSONObject(HAPContainerEntity.EXTRA));
				isEnable = HAPUtilityEntityInfo.isEnabled(extraInfo);
			}
			
			if(isEnable) {
				HAPContainerEntity entityContainer = HAPUtilityDomain.buildDefinitionContainer(containerType, eleEntityType);
				entityContainer.setExtraInfo(extraInfo);
				for(int i=0; i<eleArrayObj.length(); i++) {
					JSONObject eleObj = eleArrayObj.getJSONObject(i);
					
					//element entity
					HAPEmbededWithId embededEntity = HAPUtilityParserEntity.parseEmbededEntity(eleObj, eleEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
					
					//element info
					entityContainer.addEntityElement(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext));
				}
				entity.setContainerAttribute(attributeName, entityContainer);
			}
		}
	}

	private HAPInfoContainerElement buildContainerElementInfo(JSONObject eleObj, HAPEmbededWithId embededEntity, String containerType, HAPContextParser parserContext) {
		HAPInfoContainerElement out = null;
		if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_SET)) {
			out = new HAPInfoContainerElementSetDefinition(embededEntity);
		}
		else if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST)) {
			out = new HAPInfoContainerElementListDefinition(embededEntity);
		}
		
		out.setInfo(parserContext.getCurrentDomain().getEntityInfoDefinition(embededEntity.getEntityId()).getExtraInfo().cloneEntityInfo());
		return out;
	}
}
