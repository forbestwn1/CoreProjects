package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.Map;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextUtility;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceContextEntityProcessor {

	public static void process(HAPDefinitionUIUnit parent, HAPDefinitionUIUnit uiDefinition, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){

		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		
		Map<String, HAPContextEntity> serviceDefs = uiDefinition.getServiceDefinition();
		for(String name : serviceDefs.keySet()) {
			HAPContextEntity contextDef = serviceDefs.get(name);
			HAPContextUtility.processContextDefinitionWithoutInhert(contextDef.getContextDefinition(), parent.getContext(), contextDef.getContext(), configure, null, contextProcessorEnv);
		}
	
		Map<String, HAPContextEntity> eventDefs = uiDefinition.getEventDefinition();
		for(String name : eventDefs.keySet()) {
			HAPContextEntity contextDef = eventDefs.get(name);
			HAPContextUtility.processContextDefinitionWithoutInhert(contextDef.getContext(), parent.getContext(), contextDef.getContextDefinition(), configure, null, contextProcessorEnv);
		}

		if(uiDefinition instanceof HAPDefinitionUIUnitResource) {
			Map<String, HAPContextEntity> commandDefs = ((HAPDefinitionUIUnitResource)uiDefinition).getCommandDefinition();
			for(String name : commandDefs.keySet()) {
				HAPContextEntity contextDef = commandDefs.get(name);
				HAPContextUtility.processContextDefinitionWithoutInhert(contextDef.getContextDefinition(), parent.getContext(), contextDef.getContext(), configure, null, contextProcessorEnv);
			}
		}
		
		//children ui tags
		Iterator<HAPDefinitionUIUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPDefinitionUIUnitTag uiTag = its.next();
			process(uiDefinition, uiTag, uiTagMan, contextProcessorEnv);
		}
	}
	
}
