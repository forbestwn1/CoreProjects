package com.nosliw.data.core.domain;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
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

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPDomainEntityDefinition definitionDomain);
	

	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPDomainEntityDefinition definitionDomain) {
		HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfoDefinition(entityId).getEntity();
		return entity;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected void parseEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPDomainEntityDefinition definitionDomain) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(attrEntityType)) {
			parseComplexEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, definitionDomain);
		}
		else {
			parseSimpleEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, definitionDomain);
		}
	}
	
	protected void parseSimpleEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPDomainEntityDefinition definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededEntity attributeEntity =  HAPUtilityParserEntity.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}

	protected void parseComplexEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPDomainEntityDefinition definitionDomain) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededEntity attributeEntity =  HAPUtilityParserEntity.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setSimpleAttribute(attributeName, attributeEntity);
		}
	}
	
	protected void parseContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPDomainEntityDefinition definitionDomain) {
		if(this.m_runtimeEnv.getDomainEntityManager().isComplexEntity(eleEntityType)) {
			parseComplexContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, null, definitionDomain);
		}
		else {
			parseSimpleContainerAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, definitionDomain);
		}
	}
	
	protected void parseComplexContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPDomainEntityDefinition definitionDomain) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfoDefinition(entityId).getEntity();
			HAPContextParser parserContext = HAPUtilityDomain.getContextParse(entityId, definitionDomain);
			
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
				HAPContainerEntityImp entityContainer = HAPUtilityDomain.buildContainer(containerType);
				entityContainer.setExtraInfo(extraInfo);
				for(int i=0; i<eleArrayObj.length(); i++) {
					JSONObject eleObj = eleArrayObj.getJSONObject(i);
					
					//element entity
					HAPEmbededEntity embededEntity = HAPUtilityParserEntity.parseEmbededComplexEntity(eleObj, eleEntityType, adapterType, entityId, parentRelationConfigureCustomer, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
					
					//element info
					entityContainer.addEntityElement(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext.getDefinitionDomain()));
				}
			}
		}
	}
	
	protected void parseSimpleContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPDomainEntityDefinition definitionDomain) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			HAPDefinitionEntityInDomain entity = definitionDomain.getEntityInfoDefinition(entityId).getEntity();
			HAPContextParser parserContext = HAPUtilityDomain.getContextParse(entityId, definitionDomain);
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
				HAPContainerEntityImp entityContainer = HAPUtilityDomain.buildContainer(containerType);
				entityContainer.setExtraInfo(extraInfo);
				for(int i=0; i<eleArrayObj.length(); i++) {
					JSONObject eleObj = eleArrayObj.getJSONObject(i);
					
					//element entity
					HAPEmbededEntity embededEntity = HAPUtilityParserEntity.parseEmbededEntity(eleObj, eleEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
					
					//element info
					entityContainer.addEntityElement(buildContainerElementInfo(eleObj, embededEntity, containerType, parserContext.getDefinitionDomain()));
				}
				entity.setContainerAttribute(attributeName, entityContainer);
			}
		}
	}

	private HAPInfoContainerElement buildContainerElementInfo(JSONObject eleObj, HAPEmbededEntity embededEntity, String containerType, HAPDomainEntityDefinition definitionDomain) {
		HAPInfoContainerElement out = null;
		JSONObject eleInfoObj = eleObj.optJSONObject(HAPContainerEntity.ELEMENT_INFO);
		if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_SET)) {
			out = new HAPInfoContainerElementSet(embededEntity);
		}
		else if(containerType.equals(HAPConstantShared.ENTITYCONTAINER_TYPE_LIST)) {
			out = new HAPInfoContainerElementList(embededEntity);
		}
		out.buildObject(eleInfoObj, HAPSerializationFormat.JSON);
		if(out.getElementName()==null) {
			//if no name for element, use name from entity definition
			out.setElementName(definitionDomain.getEntityInfoDefinition(embededEntity.getEntityId()).getExtraInfo().getName());
		}
		return out;
	}
}
