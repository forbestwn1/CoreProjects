package com.nosliw.uiresource.page.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.component.event.HAPProcessEvent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIEvent {

	public static void processEvent(HAPExecutableUIUnit uiExe, HAPRuntimeEnvironment runtimeEnv) {
		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 

		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		//process relative element in event defined in resource
		List<HAPDefinitionEvent> eventsDef = uiUnitDef.getEvents();
		for(HAPDefinitionEvent event : eventsDef) {
			HAPExecutableEvent eventExe = HAPProcessEvent.processEventDefinition(event, uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure(), runtimeEnv);
			uiExe.getBody().addEventDefinition(eventExe);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processEvent(childTag, runtimeEnv);
		}
	}
	
	public static void escalateEvent(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();

		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
				List<HAPExecutableEvent> mappedEventDefs = new ArrayList<HAPExecutableEvent>();
				
				Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_EVENT));
				exeTag.setEventMapping(nameMapping);
				List<HAPExecutableEvent> exeEventDefs = body.getEvents();
				for(HAPExecutableEvent event : exeEventDefs) {
					HAPExecutableEvent mappedEvent = event.cloneExeEvent();
					String eventName = mappedEvent.getName();
					String mappedName = nameMapping.get(eventName);
					if(mappedName==null)   mappedName = eventName;
					mappedEvent.setName(mappedName);
					mappedEventDefs.add(mappedEvent);
				}
				escalate(exeTag, mappedEventDefs);
			}
			
		}

		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			escalateEvent(childTag, uiTagMan);
		}
	}

	private static void escalate(HAPExecutableUIUnit exeUnit, List<HAPExecutableEvent> eventsDef) {
		HAPExecutableUIBody body = exeUnit.getBody();
		HAPExecutableUIUnit parent = exeUnit.getParent();
		if(parent==null) {
			for(HAPExecutableEvent event : eventsDef) {
				if(body.getEventDefinition(event.getName())==null) {
					body.addEventDefinition(event);
				}
			}
		}
		else {
			escalate(parent, eventsDef);
		}
	}
}
