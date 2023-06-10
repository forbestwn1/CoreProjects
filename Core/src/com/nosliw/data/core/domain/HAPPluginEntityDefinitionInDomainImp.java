package com.nosliw.data.core.domain;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.container.HAPContainerEntity;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainer;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;
import com.nosliw.data.core.domain.container.HAPUtilityContainerEntity;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoValueType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImp implements HAPPluginEntityDefinitionInDomain{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Class<? extends HAPDefinitionEntityInDomain> m_entityClass;
	
	private String m_entityType;
	
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
			entity.setEntityType(this.getEntityType());
			
			//add to domain
			out = parserContext.getCurrentDomain().addEntity(entity);

			//parse entity content
			this.parseDefinitionContent(out, obj, parserContext);

			//plugin can do do something after parse
			postParseDefinitionContent(out, parserContext);
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
			
			//plugin can do do something after parse
			postParseDefinitionContent(entityId, parserContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);
	
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {}
	
	protected JSONObject convertToJsonObject(Object obj) {
		JSONObject out = null;
		if(obj instanceof JSONObject) out = (JSONObject)obj;
		else if(obj instanceof String)  out = new JSONObject(HAPUtilityJson.formatJson((String)obj));
		return out;
	}

	protected HAPInfoEntityInDomainDefinition getEntityDefinitionInfo(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return parserContext.getGlobalDomain().getEntityInfoDefinition(entityId);
	}
	
	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return this.getEntityDefinitionInfo(entityId, parserContext).getEntity();
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	protected void parseNormalEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(attrEntityType)) {
			parseNormalComplexEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, parserContext);
		}
		else {
			parseNormalSimpleEntityAttribute(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}

	protected void parseNormalEntityAttributeSelf(Object entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(attrEntityType)) {
			parseNormalComplexEntityAttributeSelf(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, parserContext);
		}
		else {
			parseNormalSimpleEntityAttributeSelf(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}
		
	protected void parseNormalSimpleEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			this.parseNormalSimpleEntityAttributeSelf(attrEntityObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}
	
	protected void parseNormalSimpleEntityAttributeSelf(Object attrEntityObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
		HAPEmbededDefinition attributeEntity =  HAPUtilityParserEntity.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
		entity.setNormalAttribute(attributeName, attributeEntity, new HAPInfoValueType(attrEntityType, false));
	}

	protected void parseNormalComplexEntityAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			this.parseNormalComplexEntityAttributeSelf(attrEntityObj, entityId, attributeName, attrEntityType, adapterType, parentRelationConfigureDefault, parserContext);
		}
	}

	protected void parseNormalComplexEntityAttributeSelf(Object attrEntityObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
		HAPEmbededDefinition attributeEntity =  HAPUtilityParserEntity.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
		entity.setNormalAttribute(attributeName, attributeEntity, new HAPInfoValueType(attrEntityType, true));
	}
	
	protected void parseContainerAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(eleEntityType)) {
			parseContainerComplexAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, null, parserContext);
		}
		else {
			parseContainerSimpleAttribute(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, parserContext);
		}
	}
	
	protected void parseContainerAttributeSelf(Object entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(eleEntityType)) {
			parseContainerComplexAttributeSelf(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, null, parserContext);
		}
		else {
			parseContainerSimpleAttributeSelf(entityJsonObj, entityId, attributeName, eleEntityType, adapterType, containerType, parserContext);
		}
	}
	
	protected void parseContainerComplexAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			this.parseContainerComplexAttributeSelf(containerObj, entityId, attributeName, eleEntityType, adapterType, containerType, parentRelationConfigureDefault, parserContext);
		}
	}
	
	protected void parseContainerComplexAttributeSelf(Object containerObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
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
			extraInfo = HAPUtilityEntityInfo.buildEntityInfoFromJson(containerJsonObj.optJSONObject(HAPContainerEntity.INFO));
			isEnable = HAPUtilityEntityInfo.isEnabled(extraInfo);
		}
		else if(containerObj instanceof JSONArray) {
			eleArrayObj = (JSONArray)containerObj;
		}

		if(isEnable) {
			HAPContainerEntityDefinition entityContainer = HAPUtilityContainerEntity.buildDefinitionContainer(containerType);
			entityContainer.setExtraInfo(extraInfo);
			for(int i=0; i<eleArrayObj.length(); i++) {
				JSONObject eleObj = eleArrayObj.getJSONObject(i);
				
				//element entity
				HAPEmbededDefinition embededEntity = HAPUtilityParserEntity.parseEmbededComplexEntity(eleObj, eleEntityType, adapterType, entityId, parentRelationConfigureCustomer, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
				
				//element
				entityContainer.addEntityElement(buildContainerElement(eleObj, embededEntity, parserContext));
			}
			entity.setContainerAttributeComplex(attributeName, entityContainer, eleEntityType);
		}
	}
	
	protected void parseContainerSimpleAttribute(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		Object containerObj = entityJsonObj.opt(attributeName);
		if(containerObj!=null) {
			this.parseContainerSimpleAttributeSelf(containerObj, entityId, attributeName, eleEntityType, adapterType, containerType, parserContext);
		}
	}

	protected void parseContainerSimpleAttributeSelf(Object containerObj, HAPIdEntityInDomain entityId, String attributeName, String eleEntityType, String adapterType, String containerType, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
		boolean isEnable = true;
		HAPEntityInfo extraInfo = null;
		JSONArray eleArrayObj = null;
		if(containerObj instanceof JSONArray)  eleArrayObj = (JSONArray)containerObj;
		else if(containerObj instanceof JSONObject) {
			JSONObject containerJsonObj = (JSONObject)containerObj;
			eleArrayObj = containerJsonObj.getJSONArray(HAPContainerEntity.ELEMENT);
			extraInfo = HAPUtilityEntityInfo.buildEntityInfoFromJson(containerJsonObj.optJSONObject(HAPContainerEntity.INFO));
			isEnable = HAPUtilityEntityInfo.isEnabled(extraInfo);
		}
		
		if(isEnable) {
			HAPContainerEntityDefinition entityContainer = HAPUtilityContainerEntity.buildDefinitionContainer(containerType);
			entityContainer.setExtraInfo(extraInfo);
			for(int i=0; i<eleArrayObj.length(); i++) {
				JSONObject eleObj = eleArrayObj.getJSONObject(i);
				
				//element entity
				HAPEmbededDefinition embededEntity = HAPUtilityParserEntity.parseEmbededEntity(eleObj, eleEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
				
				//element info
				entityContainer.addEntityElement(buildContainerElement(eleObj, embededEntity, parserContext));
			}
			entity.setContainerAttributeSimple(attributeName, entityContainer, eleEntityType);
		}
	}
	
	private HAPElementContainer buildContainerElement(JSONObject eleObj, HAPEmbededDefinition embededEntity, HAPContextParser parserContext) {
		HAPElementContainer out = null;
		HAPIdEntityInDomain eleEntityId = (HAPIdEntityInDomain)embededEntity.getValue();
		out = new HAPElementContainerDefinition(embededEntity, eleEntityId.toString());
		out.setInfo(parserContext.getCurrentDomain().getEntityInfoDefinition(eleEntityId).getExtraInfo().cloneEntityInfo());
		return out;
	}
}
