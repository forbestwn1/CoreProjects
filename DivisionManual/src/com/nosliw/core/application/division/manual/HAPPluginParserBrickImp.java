package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImp implements HAPPluginParserBrick{

	private Class<? extends HAPManualBrick> m_brickClass;

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginParserBrickImp(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass, HAPManualManagerBrick manualBrickMan, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickTypeId = brickTypeId;
		this.m_brickClass = brickClass;
	}
	
	@Override
	public HAPIdBrickType getBrickType() {
		return this.m_brickTypeId;
	}

	@Override
	public HAPManualBrick newBrick() {
		HAPManualBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setManualBrickManager(this.getManualDivisionEntityManager());
			out.init();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	
	@Override
	public HAPManualBrick parse(Object content, HAPSerializationFormat format, HAPManualContextParse parseContext) {
		HAPManualBrick out = null;
		
		try {
			out = this.newBrick();
			
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

	protected void postNewInstance(HAPManualBrick brickDefinition) {}
	
	
	protected void preParseDefinitionContent(HAPManualBrick brick, Object obj, HAPSerializationFormat format) {}
	
	protected void postParseDefinitionContent(HAPManualBrick brick) {}
	

	protected void parseDefinitionContentJson(HAPManualBrick brickManual, Object jsonValue, HAPManualContextParse parseContext) {}

	protected void parseDefinitionContentHtml(HAPManualBrick brickManual, Object obj, HAPManualContextParse parseContext) {}

	protected void parseDefinitionContentJavascript(HAPManualBrick brickManual, Object obj, HAPManualContextParse parseContext) {}

	protected void parseDefinitionContent(HAPManualBrick entityDefinition, Object obj, HAPSerializationFormat format, HAPManualContextParse parseContext) {}
	
	private void processReservedAttribute(HAPManualBrick entity, String attrName) {
		String attrEntityValueType = entity.getAttribute(attrName).getValueTypeInfo().getValueType();
		if(attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT)||attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			entity.getAttribute(attrName).setAttributeAutoProcess(false);
		}
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.getRuntimeEnvironment().getBrickManager();     }
	protected HAPManualManagerBrick getManualDivisionEntityManager() {    return this.m_manualBrickMan;     }
	
	//*************************************   Json format parse helper
	protected void parseBrickAttributeJson(HAPManualBrick parentEntity, JSONObject jsonObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualContextParse parserContext) {
		JSONObject attrEntityObj = jsonObj.optJSONObject(attributeName);
		if(attrEntityObj!=null) {
			parseBrickAttributeSelfJson(parentEntity, attrEntityObj, attributeName, entityTypeIfNotProvided, adapterTypeId, parserContext);
		}

	}
	
	protected void parseBrickAttributeSelfJson(HAPManualBrick parentEntity, JSONObject attrEntityObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualContextParse parserContext) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attribute = HAPManualUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext, this.m_manualBrickMan, this.getBrickManager());
			parentEntity.setAttribute(attribute);
		}
	}

	
	
	
	

	/*
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
	
	protected void parseSimpleEntityAttributeSelfJson(HAPManualBrick parentEntityDefinition, JSONObject attrEntityObj, String attributeName, HAPIdBrickType attrEntityType, HAPIdBrickType adapterType, HAPManualContextParse parseContext, HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick entityManager) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualAttribute attributeEntity =  HAPManualUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, attrEntityType, adapterType, parseContext, manualDivisionEntityMan, entityManager);
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
			HAPManualBrick entity = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId).getEntity();
			HAPEmbededDefinition attributeEntity =  HAPManualUtilityParserBrickFormatJson.parseEmbededComplexEntity(attrEntityObj, attrEntityType, adapterType, entityId, parentRelationConfigureDefault, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
			entity.setAttribute(attributeName, attributeEntity, new HAPInfoBrickType(attrEntityType, true));
			processReservedAttribute(entity, attributeName);
			out = (HAPIdEntityInDomain)attributeEntity.getValue();
		}
		return out;
	}
	
	*/
	
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
