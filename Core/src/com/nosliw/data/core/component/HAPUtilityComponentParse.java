package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPUtilityComponentParse {

	public static void parseComponentChild(HAPComponentChildImp child, JSONObject jsonObj) {
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
			HAPAttachmentUtility.parseDefinition(pageInfoObj, complexResourceDef.getAttachmentContainer());
		}
		
		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPComponentImp.CONTEXT);
		if(contextJsonObj!=null) {
			complexResourceDef.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
	}
	
	public static void parseComponent(HAPComponentImp component, JSONObject jsonObj) {
		parseComplextResourceDefinition(component, jsonObj);
		
		//lifecycle
		parseLifecycleAction(component, jsonObj);

		//event handler
		parseEventHandler(component, jsonObj);
	}

	public static HAPWithEventHanlder parseEventHandler(HAPWithEventHanlder withEventHandler, JSONObject jsonObj) {
		JSONArray eventHandlersArray = jsonObj.optJSONArray(HAPWithEventHanlder.EVENTHANDLER);
		if(eventHandlersArray!=null) {
			for(int i=0; i<eventHandlersArray.length(); i++) {
				withEventHandler.addEventHandler(HAPHandlerEvent.newInstance(eventHandlersArray.getJSONObject(i)));
			}
		}
		return withEventHandler;
	}
	
	public static HAPWithLifecycleAction parseLifecycleAction(HAPWithLifecycleAction withLifecycelAction, JSONObject jsonObj) {
		JSONArray lifecycleActionArray = jsonObj.optJSONArray(HAPWithLifecycleAction.LIFECYCLE);
		if(lifecycleActionArray!=null) {
			for(int i=0; i<lifecycleActionArray.length(); i++) {
				withLifecycelAction.addLifecycleAction(HAPHandlerLifecycle.newInstance(lifecycleActionArray.getJSONObject(i)));
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

}
