package com.nosliw.uiresource.page.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIEvent {

	public static void escalateEvent(HAPExecutableUIUnit1 exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIUnit body = exeUnit.getBody();

		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUITag exeTag = (HAPExecutableUITag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
				List<HAPExecutableEvent> mappedEventDefs = new ArrayList<HAPExecutableEvent>();
				
				Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_EVENT));
				exeTag.setEventMapping(nameMapping);
				for(String eventName : body.getEventNames()) {
					HAPExecutableEvent event = body.getEvent(eventName);
					HAPExecutableEvent mappedEvent = event.cloneExeEvent();
					String mappedName = nameMapping.get(eventName);
					if(mappedName==null)   mappedName = eventName;
					mappedEvent.setName(mappedName);
					mappedEventDefs.add(mappedEvent);
				}
				escalate(exeTag, mappedEventDefs);
			}
		}

		//child tag
		for(HAPExecutableUITag childTag : body.getUITags()) {
			escalateEvent(childTag, uiTagMan);
		}
	}

	private static void escalate(HAPExecutableUIUnit1 exeUnit, List<HAPExecutableEvent> eventsDef) {
		HAPExecutableUIUnit body = exeUnit.getBody();
		HAPExecutableUIUnit1 parent = exeUnit.getParent();
		if(parent==null) {
			for(HAPExecutableEvent event : eventsDef) {
				if(body.getEvent(event.getName())==null) {
					body.addEvent(event);
				}
			}
		}
		else {
			escalate(parent, eventsDef);
		}
	}
}
