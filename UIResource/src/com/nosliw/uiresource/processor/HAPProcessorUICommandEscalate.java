package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPProcessorUICommandEscalate {

	public static void process(HAPExecutableUIUnit exeUnit, HAPUITagManager uiTagMan) {
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getContext())) {
				Map<String, HAPContextEntity> mappedCommandDefs = new LinkedHashMap<String, HAPContextEntity>();
				
				Map<String, String> nameMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstant.UITAG_PARM_COMMAND));
				exeTag.setCommandMapping(nameMapping);
				Map<String, HAPContextEntity> exeCommandDefs = exeTag.getCommandDefinitions();
				for(String commandName : exeCommandDefs.keySet()) {
					String mappedName = nameMapping.get(commandName);
					if(mappedName==null)   mappedName = commandName;
					mappedCommandDefs.put(mappedName, exeCommandDefs.get(commandName));
				}
				escalate(exeTag.getParent(), mappedCommandDefs, uiTagMan);
			}
		}

		//child tag
		for(HAPExecutableUIUnitTag childTag : exeUnit.getUITags()) {
			process(childTag, uiTagMan);
		}
	}
	
	private static void escalate(HAPExecutableUIUnit exeUnit, Map<String, HAPContextEntity> commandsDef, HAPUITagManager uiTagMan) {
		if(HAPConstant.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
			for(String eventName : commandsDef.keySet()) {
				if(exeUnit.getEventDefinition(eventName)==null) {
					exeUnit.addEventDefinition(eventName, commandsDef.get(eventName));
				}
			}
		}
		else {
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(((HAPExecutableUIUnitTag)exeUnit).getUIUnitTagDefinition().getTagName())).getContext())) {
				escalate(exeUnit.getParent(), commandsDef, uiTagMan);
			}
		}
	}
}
