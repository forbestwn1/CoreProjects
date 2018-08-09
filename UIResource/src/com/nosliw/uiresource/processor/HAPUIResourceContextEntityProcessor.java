package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.Map;

import com.nosliw.data.context.HAPConfigureContextProcessor;
import com.nosliw.data.context.HAPContextUtility;
import com.nosliw.data.context.HAPEnvContextProcessor;
import com.nosliw.uiresource.page.HAPContextEntity;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceContextEntityProcessor {

	public static void process(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){

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

		if(uiDefinition instanceof HAPUIDefinitionUnitResource) {
			Map<String, HAPContextEntity> commandDefs = ((HAPUIDefinitionUnitResource)uiDefinition).getCommandDefinition();
			for(String name : commandDefs.keySet()) {
				HAPContextEntity contextDef = commandDefs.get(name);
				HAPContextUtility.processContextDefinitionWithoutInhert(contextDef.getContextDefinition(), parent.getContext(), contextDef.getContext(), configure, null, contextProcessorEnv);
			}
		}
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			process(uiDefinition, uiTag, uiTagMan, contextProcessorEnv);
		}
	}
	
}
