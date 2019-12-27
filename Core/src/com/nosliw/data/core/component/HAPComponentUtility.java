package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;

public class HAPComponentUtility {

	public static void solveExternalMapping(HAPComponent component, HAPDefinitionExternalMapping parentExternalMapping) {
		component.getExternalMapping().merge(parentExternalMapping, HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
	}

	public static void parseComponent(HAPComponent component, JSONObject jsonObj) {
		component.buildEntityInfoByJson(jsonObj);
		
		//parse external
		JSONObject pageInfoObj = jsonObj.optJSONObject(HAPWithExternalMapping.EXTERNALMAPPING);
		if(pageInfoObj!=null) {
			HAPExternalMappingUtility.parseDefinition(pageInfoObj, component.getExternalMapping());
		}
		
		//service
		JSONArray serviceUseListJson = jsonObj.optJSONArray(HAPComponent.SERVICE);
		if(serviceUseListJson!=null) {
			for(int i=0; i<serviceUseListJson.length(); i++) {
				JSONObject serviceUseJson = serviceUseListJson.getJSONObject(i);
				HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
				serviceUseDef.buildObject(serviceUseJson, HAPSerializationFormat.JSON);
				component.addServiceUseDefinition(serviceUseDef);
			}
		}

		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPComponent.CONTEXT);
		if(contextJsonObj!=null) {
			component.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
	}

	//build external mapping for internal component
	public static HAPDefinitionExternalMapping buildInternalComponentExternalMapping(HAPResourceId resourceId, HAPDefinitionExternalMapping externalMapping, HAPWithNameMapping withNameMapping) {
		HAPDefinitionExternalMapping out = withNameMapping.getNameMapping().mapExternal(externalMapping);
		out.merge(new HAPDefinitionExternalMapping(resourceId.getSupplement()), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		return out;
	}
	
}
