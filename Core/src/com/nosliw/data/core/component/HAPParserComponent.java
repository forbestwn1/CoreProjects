package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPWithEvent;
import com.nosliw.data.core.component.valuestructure.HAParserComponentValueStructure;
import com.nosliw.data.core.handler.HAPHandler;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPParserComponent {

	public static void parseComponentChild(HAPEmbededComponent child, JSONObject jsonObj) {
		child.buildEntityInfoByJson(jsonObj);
		
		child.setNameMapping(HAPNameMapping.newNamingMapping(jsonObj.optJSONObject(HAPWithNameMapping.NAMEMAPPING)));
		
		//event handler
		parseEventHandler(child, jsonObj);
	}

	public static void parseComplextResourceDefinition(HAPResourceDefinitionComplexImp complexResourceDef, JSONObject jsonObj) {
		//entity info
		complexResourceDef.buildEntityInfoByJson(jsonObj);
		
		//parse attachment
		JSONObject pageInfoObj = jsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
		if(pageInfoObj!=null) {
			HAPUtilityAttachment.parseDefinition(pageInfoObj, complexResourceDef.getAttachmentContainer());
		}
		
		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPComponentImp.VALUESTRUCTURE);
		if(contextJsonObj!=null) {
			complexResourceDef.setValueStructure(HAParserComponentValueStructure.parseComponentValueStructure(contextJsonObj));
		}
	}
	
	public static void parseComponent(HAPComponentImp component, JSONObject jsonObj) {
		parseComplextResourceDefinition(component, jsonObj);
		
		//lifecycle
		parseLifecycleAction(component, jsonObj);

		//event handler
		parseEventHandler(component, jsonObj);
		
		parseCommmendDefinition(component, jsonObj);

		parseEventDefinition(component, jsonObj);

	}

	public static HAPWithEventHanlder parseEventHandler(HAPWithEventHanlder withEventHandler, JSONObject jsonObj) {
		JSONArray eventHandlersArray = jsonObj.optJSONArray(HAPWithEventHanlder.EVENTHANDLER);
		if(eventHandlersArray!=null) {
			for(int i=0; i<eventHandlersArray.length(); i++) {
				HAPHandler eventHandlerTask = new HAPHandler();
				eventHandlerTask.buildObject(eventHandlersArray.getJSONObject(i), HAPSerializationFormat.JSON);
				withEventHandler.addEventHandler(eventHandlerTask);
			}
		}
		return withEventHandler;
	}
	
	public static HAPWithLifecycleAction parseLifecycleAction(HAPWithLifecycleAction withLifecycelAction, JSONObject jsonObj) {
		JSONArray lifecycleActionArray = jsonObj.optJSONArray(HAPWithLifecycleAction.LIFECYCLE);
		if(lifecycleActionArray!=null) {
			for(int i=0; i<lifecycleActionArray.length(); i++) {
				HAPHandlerLifecycle lifecycleAction = new HAPHandlerLifecycle();
				lifecycleAction.buildObject(lifecycleActionArray.getJSONObject(i), HAPSerializationFormat.JSON);
				withLifecycelAction.addLifecycleAction(lifecycleAction);
			}
		}
		return withLifecycelAction;
	}
	
	public static void parseServiceUseDefinition(HAPWithServiceUse serviceUse, JSONObject jsonObj) {
		//service
		JSONArray serviceUseListJson = jsonObj.optJSONArray(HAPWithServiceUse.SERVICE);
		if(serviceUseListJson!=null) {
			for(int i=0; i<serviceUseListJson.length(); i++) {
				JSONObject serviceUseJson = serviceUseListJson.getJSONObject(i);
				HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
				serviceUseDef.buildObject(serviceUseJson, HAPSerializationFormat.JSON);
				serviceUse.addServiceUseDefinition(serviceUseDef);
			}
		}
	}

	public static void parseCommmendDefinition(HAPWithCommand withCommand, JSONObject jsonObj) {
		JSONArray commandListJson = jsonObj.optJSONArray(HAPWithCommand.COMMAND);
		if(commandListJson!=null) {
			for(int i=0; i<commandListJson.length(); i++) {
				JSONObject commandJson = commandListJson.getJSONObject(i);
				HAPDefinitionCommand commandDef = new HAPDefinitionCommand();
				commandDef.buildObject(commandJson, HAPSerializationFormat.JSON);
				withCommand.addCommand(commandDef);
			}
		}
	}

	public static void parseEventDefinition(HAPWithEvent withEvent, JSONObject jsonObj) {
		JSONArray eventListJson = jsonObj.optJSONArray(HAPWithEvent.EVENT);
		if(eventListJson!=null) {
			for(int i=0; i<eventListJson.length(); i++) {
				JSONObject eventJson = eventListJson.getJSONObject(i);
				HAPDefinitionEvent eventDef = new HAPDefinitionEvent();
				eventDef.buildObject(eventJson, HAPSerializationFormat.JSON);
				withEvent.addEvent(eventDef);
			}
		}
	}

}
