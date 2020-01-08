package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

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
		
		//context
		JSONObject contextJsonObj = jsonObj.optJSONObject(HAPComponentImp.CONTEXT);
		if(contextJsonObj!=null) {
			component.setContext(HAPParserContext.parseContextGroup(contextJsonObj));
		}
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

	public static HAPAttachmentContainer buildNameMappedAttachment(HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = withNameMapping.getNameMapping().mapAttachment(attachment);
		return out;
	}

	
	//build attachment mapping for internal component
	public static HAPAttachmentContainer buildInternalAttachment(HAPResourceId resourceId, HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = buildNameMappedAttachment(attachment, withNameMapping); 
		if(resourceId!=null) {
			out.merge(new HAPAttachmentContainer(resourceId.getSupplement()), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		return out;
	}
	
	public static void buildServiceChildrenComponent(HAPChildrenComponentIdContainer out, HAPWithServiceUse withServiceProvider, HAPAttachmentContainer attachment) {
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = withServiceProvider.getServiceProviderDefinitions(); 
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = withServiceProvider.getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			out.addChildCompoentId(new HAPChildrenComponentId(serviceName, HAPResourceId.newInstance(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE, serviceProvider.getServiceId()), null), attachment);
		}
	}
	
}
