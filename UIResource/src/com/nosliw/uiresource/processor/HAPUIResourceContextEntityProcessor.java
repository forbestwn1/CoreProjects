package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.context.HAPContextUtility;
import com.nosliw.uiresource.page.HAPContextEntity;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceContextEntityProcessor {

	public static void process(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

		Map<String, HAPContextEntity> serviceDefs = uiDefinition.getServiceDefinition();
		for(String name : serviceDefs.keySet()) {
			HAPContextEntity contextDef = serviceDefs.get(name);
			HAPContextUtility.processContextDefinition(parent.getContext(), contextDef.getContextDefinition(), contextDef.getContext(), null, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	
		Map<String, HAPContextEntity> eventDefs = uiDefinition.getEventDefinition();
		for(String name : eventDefs.keySet()) {
			HAPContextEntity contextDef = eventDefs.get(name);
			HAPContextUtility.processContextDefinition(parent.getContext(), contextDef.getContextDefinition(), contextDef.getContext(), null, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}

		if(uiDefinition instanceof HAPUIDefinitionUnitResource) {
			Map<String, HAPContextEntity> commandDefs = ((HAPUIDefinitionUnitResource)uiDefinition).getCommandDefinition();
			for(String name : commandDefs.keySet()) {
				HAPContextEntity contextDef = commandDefs.get(name);
				HAPContextUtility.processContextDefinition(parent.getContext(), contextDef.getContextDefinition(), contextDef.getContext(), null, dataTypeHelper, uiTagMan, runtime, expressionManager);
			}
		}
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			process(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	}
	
}
