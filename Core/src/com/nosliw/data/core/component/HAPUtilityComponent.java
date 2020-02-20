package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPUtilityComponent {

	public static HAPContextGroup processElementComponentContext(HAPComponentContainerElement component, HAPContextGroup extraContext, HAPRequirementContextProcessor contextProcessRequirement, HAPConfigureContextProcessor processConfigure) {
		HAPContextGroup parentContext = HAPUtilityContext.hardMerge(component.getContext(), extraContext); 
		return HAPProcessorContext.process(component.getElement().getContext(), HAPParentContext.createDefault(parentContext), processConfigure, contextProcessRequirement);
	}

	
	public static void mergeWithParentAttachment(HAPWithAttachment withAttachment, HAPAttachmentContainer parentAttachment) {
		withAttachment.getAttachmentContainer().merge(parentAttachment, HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD);
	}
	
	public static HAPAttachmentContainer buildNameMappedAttachment(HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = null;
		if(withNameMapping==null)   out = attachment;
		else out = withNameMapping.getNameMapping().mapAttachment(attachment);
		if(out==null)  out = new HAPAttachmentContainer();
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

	public static void buildServiceChildrenComponent(HAPChildrenComponentIdContainer out, HAPWithServiceUse withServiceProvider, HAPAttachmentContainer attachment) {
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = withServiceProvider.getServiceProviderDefinitions(); 
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = withServiceProvider.getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			out.addChildCompoentId(new HAPChildrenComponentId(serviceName, HAPResourceIdFactory.newInstance(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE, serviceProvider.getServiceId()), null), attachment);
		}
	}
}
