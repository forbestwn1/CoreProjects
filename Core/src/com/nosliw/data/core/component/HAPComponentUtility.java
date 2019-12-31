package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;

public class HAPComponentUtility {

	public static void solveAttachment(HAPComponentImp component, HAPAttachmentContainer parentAttachment) {
		component.getAttachmentContainer().merge(parentAttachment, HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
	}

	public static void parseComponent(HAPComponentImp component, JSONObject jsonObj) {
		component.buildEntityInfoByJson(jsonObj);
		
		//parse attachment
		JSONObject pageInfoObj = jsonObj.optJSONObject(HAPWithAttachment.ATTACHMENT);
		if(pageInfoObj!=null) {
			HAPAttachmentUtility.parseDefinition(pageInfoObj, component.getAttachmentContainer());
		}
		
		//service
		JSONArray serviceUseListJson = jsonObj.optJSONArray(HAPComponentImp.SERVICE);
		if(serviceUseListJson!=null) {
			for(int i=0; i<serviceUseListJson.length(); i++) {
				JSONObject serviceUseJson = serviceUseListJson.getJSONObject(i);
				HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
				serviceUseDef.buildObject(serviceUseJson, HAPSerializationFormat.JSON);
				component.addServiceUseDefinition(serviceUseDef);
			}
		}

		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPComponentImp.CONTEXT);
		if(contextJsonObj!=null) {
			component.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
	}

	//build attachment mapping for internal component
	public static HAPAttachmentContainer buildInternalAttachment(HAPResourceId resourceId, HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = withNameMapping.getNameMapping().mapAttachment(attachment);
		if(resourceId!=null) {
			out.merge(new HAPAttachmentContainer(resourceId.getSupplement()), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		return out;
	}
	
}
