package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.command.HAPWithCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPDefinitionHandlerEvent;
import com.nosliw.data.core.component.event.HAPWithEvent;
import com.nosliw.data.core.component.valuestructure.HAParserComponentValueStructure;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPUtilityTask;

public class HAPParserComponent {

	public static void parseEmbededComponent(HAPDefinitionEmbededComponent embededComponent, JSONObject jsonObj) {
		
	}
	
	public static void parseComponent(HAPDefinitionComponentImp component, JSONObject jsonObj, HAPManagerTask taskMan) {
		parseComplextResourceDefinition(component, jsonObj);
		
		parseCommmendDefinition(component, jsonObj);

		parseEventDefinition(component, jsonObj);
		
		parseTask(component, jsonObj, component, taskMan);

		parseServiceUseDefinition(component, jsonObj);
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
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPDefinitionComponentImp.VALUESTRUCTURE);
		if(contextJsonObj!=null) {
			complexResourceDef.setValueStructure(HAParserComponentValueStructure.parseComponentValueStructure(contextJsonObj));
		}
	}
	
	public static HAPWithTask parseTask(HAPWithTask withTask, JSONObject jsonObj, HAPDefinitionEntityComplex complexEntity, HAPManagerTask taskMan) {
		JSONArray tasksArray = jsonObj.optJSONArray(HAPWithTask.TASK);
		if(tasksArray!=null) {
			for(int i=0; i<tasksArray.length(); i++) {
				withTask.getTaskSuite().addEntityElement(HAPUtilityTask.parseTask(tasksArray.getJSONObject(i), complexEntity, taskMan));
			}
		}
		return withTask;
	}
	
	public static HAPWithEventHanlder parseEventHandler(HAPWithEventHanlder withEventHandler, JSONObject jsonObj) {
		JSONArray eventHandlersArray = jsonObj.optJSONArray(HAPWithEventHanlder.EVENTHANDLER);
		if(eventHandlersArray!=null) {
			for(int i=0; i<eventHandlersArray.length(); i++) {
				HAPDefinitionHandlerEvent eventHandler = new HAPDefinitionHandlerEvent();
				eventHandler.buildObject(eventHandlersArray.getJSONObject(i), HAPSerializationFormat.JSON);
				withEventHandler.addEventHandler(eventHandler);
			}
		}
		return withEventHandler;
	}
	
	public static void parseServiceUseDefinition(HAPWithService serviceUse, JSONObject jsonObj) {
		//service
		JSONArray serviceUseListJson = jsonObj.optJSONArray(HAPWithServiceUse.SERVICE);
		if(serviceUseListJson!=null) {
			for(int i=0; i<serviceUseListJson.length(); i++) {
				JSONObject serviceUseJson = serviceUseListJson.getJSONObject(i);
				HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
				serviceUseDef.buildObject(serviceUseJson, HAPSerializationFormat.JSON);
				serviceUse.addService(serviceUseDef);
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
