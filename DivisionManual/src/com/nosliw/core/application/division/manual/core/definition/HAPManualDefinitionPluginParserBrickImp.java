package com.nosliw.core.application.division.manual.core.definition;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.parm.HAPManagerParm;
import com.nosliw.common.parm.HAPParms;
import com.nosliw.common.parm.HAPWithParms;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugDefinition;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.brick.wrapperbrick.HAPManualDefinitionBrickWrapperBrick;
import com.nosliw.core.application.division.manual.common.task.HAPManualDefinitionWithBrickTasks;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionPluginParserBrickImp implements HAPManualDefinitionPluginParserBrick{

	private Class<? extends HAPManualDefinitionBrick> m_brickClass;

	private HAPManagerApplicationBrick m_brickMan;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPIdBrickType m_brickTypeId;
	
	public HAPManualDefinitionPluginParserBrickImp(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		this.m_brickTypeId = brickTypeId;
		this.m_brickClass = brickClass;
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickMan = brickMan;
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
			out.setManualBrickManager(this.getManualDivisionBrickManager());
			out.setBrickManager(this.getBrickManager());

			//create parms attribute
			if(out instanceof HAPWithParms) {
				((HAPWithParms)out).setParms(new HAPParms());
			}
			
			out.init();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	protected HAPManagerApplicationBrick getBrickManager() {    return this.m_brickMan;     }
	protected HAPManualManagerBrick getManualDivisionBrickManager() {    return this.m_manualBrickMan;     }

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
				Object jsonObj = HAPUtilityJson.toJsonObject(content);
				if(jsonObj instanceof JSONObject) {
					if(!HAPUtilityEntityInfo.isEnabled((JSONObject)jsonObj)) {
						//if disabled, then return null
						return null;
					}
				}
				this.parseDefinitionContentJson(out, jsonObj, parseContext);
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
	

	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		if(jsonValue instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)jsonValue;
			
//			this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);

			//entity info
			if(brickDefinition instanceof HAPEntityInfo) {
				HAPUtilityEntityInfo.buildEntityInfoByJson(jsonObj, (HAPEntityInfo)brickDefinition);
			}
			
			//tasks container
	        if(brickDefinition instanceof HAPManualDefinitionWithBrickTasks) {
	        	this.parseTaskContainerAttribute(brickDefinition, jsonObj, parseContext);
	        }
			
			//parms
	        if(brickDefinition instanceof HAPWithParms) {
	        	this.parseParmsAttribute((HAPWithParms)brickDefinition, jsonObj);
	        }
			
			//task interface
	        if(brickDefinition instanceof HAPWithBlockInteractiveTask) {
	        	this.parseTaskInterfaceAttribute(brickDefinition, jsonObj, parseContext);
	        }
			
	        //expression interface
	        if(brickDefinition instanceof HAPWithBlockInteractiveExpression) {
	        	this.parseExpressionInterfaceAttribute(brickDefinition, jsonObj, parseContext);
	        }

	        //variable for debug attirbute
	        if(brickDefinition instanceof HAPWithVariableDebugDefinition) {
	        	this.parseDebugVariable(brickDefinition, jsonObj, parseContext);
	        }

	        //script resource attribute
	        if(brickDefinition instanceof HAPWithScriptReference) {
	    		Object scriptObj = jsonObj.opt(HAPWithScriptReference.SCRIPTRESOURCEID);
	    		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
	    		brickDefinition.setAttributeValueWithValue(HAPWithScriptReference.SCRIPTRESOURCEID, scriptResourceId);
	        }
			
		}
	}

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
			HAPManualDefinitionBrick brickDef = HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(obj, entityTypeIfNotProvided, format, parserContext);
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
			HAPManualDefinitionAttributeInBrick attribute = HAPManualDefinitionUtilityParserBrickFormatJson.parseAttribute(attributeName, attrEntityObj, entityTypeIfNotProvided, adapterTypeId, parserContext);
			parentBrick.setAttribute(attribute);
		}
	}

	//parse task container
	protected void parseTaskContainerAttribute(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, HAPManualDefinitionContextParse parserContext) {
		HAPManualDefinitionBrickContainer taskContainer = (HAPManualDefinitionBrickContainer)parserContext.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100);
		parentBrick.setAttributeValueWithBrick(HAPManualDefinitionWithBrickTasks.TASK, taskContainer);
		JSONArray taskArrayJson = attrEntityObj.optJSONArray(HAPManualDefinitionWithBrickTasks.TASK);
		for(int i=0; i<taskArrayJson.length(); i++) {
			HAPManualDefinitionBrickWrapperBrick task = (HAPManualDefinitionBrickWrapperBrick)HAPManualDefinitionUtilityParserBrick.parseBrickDefinition(taskArrayJson.getJSONObject(i), HAPEnumBrickType.WRAPPERBRICK_100, HAPSerializationFormat.JSON, parserContext);
			if(task!=null) {
				taskContainer.addElementWithBrick(task);
			}
		}
	}
	
	//parse parm attribute
	protected void parseParmsAttribute(HAPWithParms parentBrick, JSONObject attrEntityObj) {
		JSONObject parmJsonObj = attrEntityObj.optJSONObject(HAPWithParms.PARM);
		if(parmJsonObj!=null) {
			HAPParms parms = HAPManagerParm.getInstance().parseParms(parmJsonObj);
			parentBrick.setParms(parms);
		}
	}
	
	//parse task interface
	protected void parseTaskInterfaceAttribute(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, HAPManualDefinitionContextParse parserContext) {
		this.parseBrickAttributeJson(parentBrick, attrEntityObj, HAPWithBlockInteractiveTask.TASKINTERFACE, HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, null, parserContext);
	}
	
	//parse expression interface
	protected void parseExpressionInterfaceAttribute(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, HAPManualDefinitionContextParse parserContext) {
		this.parseBrickAttributeJson(parentBrick, attrEntityObj, HAPWithBlockInteractiveExpression.EXPRESSIONINTERFACE, HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, null, parserContext);
	}
	
	//parse variable attribute for debug
	protected void parseDebugVariable(HAPManualDefinitionBrick parentBrick, JSONObject attrEntityObj, HAPManualDefinitionContextParse parserContext) {
		List<String> vars = new ArrayList<String>();
		Object varObj = attrEntityObj.opt(HAPWithVariableDebugDefinition.VARIABLE);
		if(varObj!=null) {
			if(varObj instanceof String) {
				vars.add((String)varObj);
			}
			else if(varObj instanceof JSONArray) {
				JSONArray varArrayJson = (JSONArray)varObj;
				for(int i=0; i<varArrayJson.length(); i++) {
					vars.add(varArrayJson.getString(i));
				}
			}
		}
		parentBrick.setAttributeValueWithValue(HAPWithVariableDebugDefinition.VARIABLE, vars);
	}
	
	
	
	
	protected boolean isAttributeEnabledJson(Object entityObj) {
		boolean out = true;
		if(entityObj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)entityObj;
			return HAPUtilityEntityInfo.isEnabled(jsonObj);
		}
		return out;
	}
}
