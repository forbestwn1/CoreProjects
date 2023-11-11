package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
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

	//new definition instance
	@Override
	public HAPIdEntityInDomain newInstance(HAPContextParser parserContext) {
		HAPIdEntityInDomain out = null;
		try {			
			HAPDefinitionEntityInDomain entity = this.m_entityClass.newInstance();
			entity.setEntityType(this.getEntityType());
			
			//add to domain
			out = parserContext.getCurrentDomain().addEntity(entity);
			
			this.postNewInstance(out, parserContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {}
	
	@Override
	public void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPSerializationFormat format, HAPContextParser parserContext) {
		
		//plugin can do do something before parse
		this.preParseDefinitionContent(entityId, obj, format, parserContext);
		
		//parse entity content
		switch(format) {
		case JSON:
			this.parseDefinitionContentJson(entityId, this.convertToJsonObject(obj), parserContext);
			break;
		case HTML:
			this.parseDefinitionContentHtml(entityId, obj, parserContext);
			break;
		case JAVASCRIPT:
			this.parseDefinitionContentJavascript(entityId, obj, parserContext);
			break;
		default:
			this.parseDefinitionContent(entityId, obj, format, parserContext);
		}
		
		//plugin can do do something after parse
		this.postParseDefinitionContent(entityId, parserContext);

	}

	protected void preParseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPSerializationFormat format, HAPContextParser parserContext) {}
	
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {}
	

	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {}

	protected void parseDefinitionContentHtml(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {}

	protected void parseDefinitionContentJavascript(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {}

	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPSerializationFormat format, HAPContextParser parserContext) {}
	
	protected Object convertToJsonObject(Object obj) {
		if(obj instanceof String)  return HAPUtilityJson.toJsonObject(HAPUtilityJson.formatJson((String)obj));  
		return obj;
	}

	protected HAPInfoEntityInDomainDefinition getEntityDefinitionInfo(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return parserContext.getGlobalDomain().getEntityInfoDefinition(entityId);
	}
	
	protected HAPDefinitionEntityInDomain getEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return this.getEntityDefinitionInfo(entityId, parserContext).getEntity();
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }

	private void processReservedAttribute(HAPDefinitionEntityInDomain entity, String attrName) {
		String attrEntityValueType = entity.getAttribute(attrName).getValueTypeInfo().getValueType();
		if(attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT)||attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			entity.getAttribute(attrName).setAttributeAutoProcess(false);
		}
	}
	
	//*************************************   Json format parse helper
	protected void parseEntityAttributeJson(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(attrEntityType)) {
			parseComplexEntityAttributeJson(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, parserContext);
		}
		else {
			parseSimpleEntityAttributeJson(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}

	protected void parseEntityAttributeSelfJson(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(this.m_runtimeEnv.getDomainEntityDefinitionManager().isComplexEntity(attrEntityType)) {
			parseComplexEntityAttributeSelfJson(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, null, parserContext);
		}
		else {
			parseSimpleEntityAttributeSelfJson(entityJsonObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}
		
	protected void parseSimpleEntityAttributeJson(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		Object attrEntityObj = entityJsonObj.opt(attributeName);
		if(attrEntityObj!=null) {
			this.parseSimpleEntityAttributeSelfJson(attrEntityObj, entityId, attributeName, attrEntityType, adapterType, parserContext);
		}
	}
	
	protected void parseSimpleEntityAttributeSelfJson(Object attrEntityObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPContextParser parserContext) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededDefinition attributeEntity =  HAPUtilityParserEntityFormatJson.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			
			if(attributeName==null) {
				HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attributeEntity.getValue();
				attributeName = parserContext.getGlobalDomain().getEntityInfoDefinition(attrEntityId).getExtraInfo().getName();
			}
			
			entity.setAttribute(attributeName, attributeEntity, new HAPInfoValueType(attrEntityType, false));
			processReservedAttribute(entity, attributeName);
		}
	}

	protected HAPIdEntityInDomain parseComplexEntityAttributeJson(JSONObject entityJsonObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = null;
		JSONObject attrEntityObj = entityJsonObj.optJSONObject(attributeName);
		if(attrEntityObj!=null) {
			out = this.parseComplexEntityAttributeSelfJson(attrEntityObj, entityId, attributeName, attrEntityType, adapterType, parentRelationConfigureDefault, parserContext);
		}
		return out;
	}

	protected HAPIdEntityInDomain parseComplexEntityAttributeSelfJson(JSONObject attrEntityObj, HAPIdEntityInDomain entityId, String attributeName, String attrEntityType, String adapterType, HAPConfigureParentRelationComplex parentRelationConfigureDefault, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = null;
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPDefinitionEntityInDomain entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededDefinition attributeEntity =  HAPUtilityParserEntityFormatJson.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setAttribute(attributeName, attributeEntity, new HAPInfoValueType(attrEntityType, true));
			processReservedAttribute(entity, attributeName);
			out = (HAPIdEntityInDomain)attributeEntity.getValue();
		}
		return out;
	}
	
	protected boolean isAttributeEnabledJson(Object entityObj) {
		boolean out = true;
		if(entityObj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)entityObj;
			JSONObject extraJsonObj = jsonObj.optJSONObject("extra");
			if(extraJsonObj!=null) {
				return HAPUtilityEntityInfo.isEnabled(extraJsonObj);
			}
		}
		
		return out;
	}
}
