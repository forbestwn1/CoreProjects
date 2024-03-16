package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImp implements HAPPluginParserEntity{

	private Class<? extends HAPManualEntity> m_entityClass;

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManagerEntityDivisionManual m_manualDivisionEntityMan;
	
	private HAPIdBrickType m_entityTypeId;
	
	public HAPPluginParserEntityImp(HAPIdBrickType entityTypeId, Class<? extends HAPManualEntity> entityClass, HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_manualDivisionEntityMan = manualDivisionEntityMan;
		this.m_entityTypeId = entityTypeId;
		this.m_entityClass = entityClass;
	}
	
	@Override
	public HAPIdBrickType getEntityType() {
		return this.m_entityTypeId;
	}

	@Override
	public HAPManualEntity parse(Object content, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPManualEntity out = null;
		
		try {
			out = this.m_entityClass.newInstance();
			postNewInstance(out);
			
			
			//plugin can do do something before parse
			this.preParseDefinitionContent(out, content, format);
			
			//parse entity content
			switch(format) {
			case JSON:
				this.parseDefinitionContentJson(out, HAPUtilityJson.toJsonObject(content), parseContext);
				break;
			case HTML:
				this.parseDefinitionContentHtml(out, content, parseContext);
				break;
			case JAVASCRIPT:
				this.parseDefinitionContentJavascript(out, content, parseContext);
				break;
			default:
				this.parseDefinitionContent(out, content, format, parseContext);
			}
			
			//plugin can do do something after parse
			this.postParseDefinitionContent(out);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	protected void postNewInstance(HAPManualEntity entityDefinition) {}
	
	
	protected void preParseDefinitionContent(HAPManualEntity entityDefinition, Object obj, HAPSerializationFormat format) {}
	
	protected void postParseDefinitionContent(HAPManualEntity entityDefinition) {}
	

	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {}

	protected void parseDefinitionContentHtml(HAPManualEntity entityDefinition, Object obj, HAPContextParse parseContext) {}

	protected void parseDefinitionContentJavascript(HAPManualEntity entityDefinition, Object obj, HAPContextParse parseContext) {}

	protected void parseDefinitionContent(HAPManualEntity entityDefinition, Object obj, HAPSerializationFormat format, HAPContextParse parseContext) {}
	
	private void processReservedAttribute(HAPManualEntity entity, String attrName) {
		String attrEntityValueType = entity.getAttribute(attrName).getValueTypeInfo().getValueType();
		if(attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT)||attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			entity.getAttribute(attrName).setAttributeAutoProcess(false);
		}
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }
	protected HAPManagerApplicationBrick getEntityManager() {    return this.getRuntimeEnvironment().getEntityManager();     }
	protected HAPManagerEntityDivisionManual getManualDivisionEntityManager() {    return this.m_manualDivisionEntityMan;     }
	
	//*************************************   Json format parse helper
	protected void parseEntityAttributeJson(HAPManualEntity parentEntity, JSONObject jsonObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPContextParse parserContext) {
		JSONObject attrEntityObj = jsonObj.optJSONObject(attributeName);
		if(attrEntityObj!=null) {
			parseEntityAttributeSelfJson(parentEntity, attrEntityObj, attributeName, entityTypeIfNotProvided, adapterTypeId, parserContext);
		}

	}
	
	protected void parseEntityAttributeSelfJson(HAPManualEntity parentEntity, JSONObject attrEntityObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPContextParse parserContext) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attribute = HAPUtilityParserEntityFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext, this.m_manualDivisionEntityMan, this.getEntityManager());
			parentEntity.setAttribute(attribute);
		}
	}

	
	
	
	
	
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
	
	protected void parseSimpleEntityAttributeSelfJson(HAPManualEntity parentEntityDefinition, JSONObject attrEntityObj, String attributeName, HAPIdBrickType attrEntityType, HAPIdBrickType adapterType, HAPContextParse parseContext, HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attributeEntity =  HAPUtilityParserEntityFormatJson.parseAttribute(attributeName, attrEntityObj, attrEntityType, adapterType, parseContext, manualDivisionEntityMan, entityManager);
			parentEntityDefinition.setAttribute(attributeEntity);
			
			parentEntityDefinition.setAttribute(attributeName, attributeEntity, new HAPInfoBrickType(attrEntityType, false));
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
			HAPManualEntity entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededDefinition attributeEntity =  HAPUtilityParserEntityFormatJson.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setAttribute(attributeName, attributeEntity, new HAPInfoBrickType(attrEntityType, true));
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
