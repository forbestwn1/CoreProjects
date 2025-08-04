package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.task.HAPManualDefinitionWithTaskInterfaceInteractive;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualDefinitionPluginParserBrickImp implements HAPManualDefinitionPluginParserBrick{

	private Class<? extends HAPManualDefinitionBrick> m_brickClass;

	private HAPManagerApplicationBrick m_brickMan;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPIdBrickType m_brickTypeId;
	
	public HAPManualDefinitionPluginParserBrickImp(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass) {
		this.m_brickTypeId = brickTypeId;
		this.m_brickClass = brickClass;
	}
	
	@Override
	public HAPIdBrickType getBrickType() {
		return this.m_brickTypeId;
	}

	@Override
	public HAPManualDefinitionBrick newBrick() {
		HAPManualDefinitionBrick out = null;
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
	
	@Autowired
	public void setBrickManager(HAPManagerApplicationBrick brickMan) {  this.m_brickMan = brickMan;     }
	@Autowired
	public void setManualBrickManager(HAPManualManagerBrick manualBrickMan) {   this.m_manualBrickMan = manualBrickMan;      }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.m_brickMan;     }
	protected HAPManualManagerBrick getManualDivisionEntityManager() {    return this.m_manualBrickMan;     }

	@Override
	public HAPManualDefinitionBrick parse(Object content, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrick out = null;
		
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

	protected void postNewInstance(HAPManualDefinitionBrick brickDefinition) {}
	
	
	protected void preParseDefinitionContent(HAPManualDefinitionBrick brick, Object obj, HAPSerializationFormat format) {}
	
	protected void postParseDefinitionContent(HAPManualDefinitionBrick brick) {}
	

	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {}

	protected void parseDefinitionContentHtml(HAPManualDefinitionBrick brickManual, Object obj, HAPManualDefinitionContextParse parseContext) {}

	protected void parseDefinitionContentJavascript(HAPManualDefinitionBrick brickManual, Object obj, HAPManualDefinitionContextParse parseContext) {}

	protected void parseDefinitionContent(HAPManualDefinitionBrick entityDefinition, Object obj, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {}
	
	private void processReservedAttribute(HAPManualDefinitionBrick entity, String attrName) {
		String attrEntityValueType = entity.getAttribute(attrName).getValueTypeInfo().getValueType();
		if(attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT)||attrEntityValueType.equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			entity.getAttribute(attrName).setAttributeAutoProcess(false);
		}
	}

	//*************************************   attribute parse helper
	protected void parseBrickAttribute(HAPManualDefinitionBrick parentBrick, Object obj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parserContext) {
		switch(format) {
		case JSON:
			parseBrickAttributeJson(parentBrick, (JSONObject)obj, attributeName, entityTypeIfNotProvided, adapterTypeId, parserContext);			
			break;
		case HTML:
			HAPManualDefinitionBrick brickDef = this.getManualDivisionEntityManager().parseBrickDefinition(obj, entityTypeIfNotProvided, format, parserContext);
			parentBrick.setAttributeValueWithBrick(attributeName, brickDef);
			break;
		case JAVASCRIPT:
			break;
		default:
		}
	}
	
	protected void parseBrickAttributeJson(HAPManualDefinitionBrick parentBrick, JSONObject jsonObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parserContext) {
		JSONObject attrEntityObj = jsonObj.optJSONObject(attributeName);
		if(attrEntityObj!=null) {
			parseBrickAttributeSelfJson(parentBrick, attrEntityObj, attributeName, entityTypeIfNotProvided, adapterTypeId, parserContext);
		}

	}
	
	protected void parseBrickAttributeSelfJson(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, String attributeName, HAPIdBrickType entityTypeIfNotProvided, HAPIdBrickType adapterTypeId, HAPManualDefinitionContextParse parserContext) {
		if(isAttributeEnabledJson(attrEntityObj)) {
			HAPManualDefinitionAttributeInBrick attribute = HAPManualDefinitionUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext, this.m_manualBrickMan, this.getBrickManager());
			parentBrick.setAttribute(attribute);
		}
	}

	protected void parseTaskInterfaceAttribute(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, HAPManualDefinitionContextParse parserContext) {
		this.parseBrickAttributeJson(parentBrick, attrEntityObj, HAPManualDefinitionWithTaskInterfaceInteractive.TASKINTERFACE, HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, null, parserContext);
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
