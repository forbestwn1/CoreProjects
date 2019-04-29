package com.nosliw.uiresource.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;

public class HAPUtilityParser {

	public static Map<String, HAPDefinitionEventHandler> parseEventHandlers(JSONObject eventHandlersJson, HAPManagerActivityPlugin activityPluginMan){
		Map<String, HAPDefinitionEventHandler> out = new LinkedHashMap<String, HAPDefinitionEventHandler>();
		if(eventHandlersJson!=null) {
			for(Object key : eventHandlersJson.keySet()) {
				HAPDefinitionEventHandler eventHandler = new HAPDefinitionEventHandler();
				JSONObject eventHandlerJson = eventHandlersJson.getJSONObject((String)key);
				eventHandler.setProcess(HAPParserProcessDefinition.parseEmbededProcess(eventHandlerJson.optJSONObject(HAPDefinitionEventHandler.PROCESS), activityPluginMan));
				out.put((String)key, eventHandler);
			}
		}
		return out;
	}
	
}
