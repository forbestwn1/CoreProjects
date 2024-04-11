package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public abstract class HAPPluginRepositoryBrickImp implements HAPPluginRepositoryBrick{

	private Class<? extends HAPManualBrick> m_entityClass;

	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginRepositoryBrickImp(HAPIdBrickType entityTypeId, Class<? extends HAPManualBrick> entityClass) {
		this.m_brickTypeId = entityTypeId;
		this.m_entityClass = entityClass;
	}
	
	@Override
	public HAPIdBrickType getBrickType() {
		return this.m_brickTypeId;
	}

	@Override
	public HAPManualBrick retrieveBrickDefinition(HAPIdBrick entityId) {
		HAPManualBrick out = null;
		
		try {
			out = this.m_entityClass.newInstance();
			postNewInstance(out);
			
			HAPManualInfoBrickLocation entityLocationInfo = HAPUtilityBrickLocation.getEntityLocationInfo(entityId);
			
			HAPSerializationFormat format = entityLocationInfo.getFormat();
			
			String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());
			
			//plugin can do do something before parse
			this.preParseDefinitionContent(out, content, format);
			
			//parse entity content
			switch(format) {
			case JSON:
				this.parseDefinitionContentJson(out, this.convertToJsonObject(content));
				break;
			case HTML:
				this.parseDefinitionContentHtml(out, content);
				break;
			case JAVASCRIPT:
				this.parseDefinitionContentJavascript(out, content);
				break;
			default:
				this.parseDefinitionContent(out, content, format);
			}
			
			//plugin can do do something after parse
			this.postParseDefinitionContent(out);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}

	protected void postNewInstance(HAPManualBrick entityDefinition) {}
	
	
	protected void preParseDefinitionContent(HAPManualBrick entityDefinition, Object obj, HAPSerializationFormat format) {}
	
	protected void postParseDefinitionContent(HAPManualBrick entityDefinition) {}
	

	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue) {}

	protected void parseDefinitionContentHtml(HAPManualBrick entityDefinition, Object obj) {}

	protected void parseDefinitionContentJavascript(HAPManualBrick entityDefinition, Object obj) {}

	protected void parseDefinitionContent(HAPManualBrick entityDefinition, Object obj, HAPSerializationFormat format) {}
	
	protected Object convertToJsonObject(Object obj) {
		if(obj instanceof String) {
			return HAPUtilityJson.toJsonObject((String)obj);
		}  
		return obj;
	}
	
	private void processReservedAttribute(HAPManualBrick entity, String attrName) {
		String attrEntityValueType = entity.getAttribute(attrName).getValueTypeInfo().getValueType();
		if(attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT)||attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			entity.getAttribute(attrName).setAttributeAutoProcess(false);
		}
	}

	//*************************************   Json format parse helper
	protected void parseEntityAttributeSelf(HAPManualBrick parentEntity, JSONObject attrEntityObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualContextParse parserContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attribute = HAPUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext, manualDivisionEntityMan, entityManager);
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
	
	protected void parseSimpleEntityAttributeSelfJson(HAPManualBrick parentEntity, JSONObject attrEntityObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualContextParse parserContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attribute = HAPUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext, manualDivisionEntityMan, entityManager);
			parentEntity.setAttribute(attribute);
			
//			HAPManualBrick entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
//			HAPEmbededDefinition attributeEntity =  HAPUtilityParserBrickFormatJson.parseEmbededEntity(attrEntityObj, attrEntityType, adapterType, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
//			
//			if(attributeName==null) {
//				HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attributeEntity.getValue();
//				attributeName = parserContext.getGlobalDomain().getEntityInfoDefinition(attrEntityId).getExtraInfo().getName();
//			}
//			
//			entity.setAttribute(attributeName, attributeEntity, new HAPInfoBrickType(attrEntityType, false));
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
			HAPManualBrick entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededDefinition attributeEntity =  HAPUtilityParserBrickFormatJson.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
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
