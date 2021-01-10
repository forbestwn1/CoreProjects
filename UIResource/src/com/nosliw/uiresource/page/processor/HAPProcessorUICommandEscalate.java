package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPProcessorUICommandEscalate {

	public static void process(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();

		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
				Map<String, HAPDefinitionUICommand> mappedCommandDefs = new LinkedHashMap<String, HAPDefinitionUICommand>();
				
				Map<String, String> nameMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstant.UITAG_PARM_COMMAND));
				exeTag.setCommandMapping(nameMapping);
				Map<String, HAPDefinitionUICommand> exeCommandDefs = body.getCommandDefinitions();
				for(String commandName : exeCommandDefs.keySet()) {
					String mappedName = nameMapping.get(commandName);
					if(mappedName==null)   mappedName = commandName;
					mappedCommandDefs.put(mappedName, exeCommandDefs.get(commandName));
				}
				escalate(exeTag.getParent(), mappedCommandDefs, uiTagMan);
			}
		}

		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			process(childTag, uiTagMan);
		}
	}
	
	private static void escalate(HAPExecutableUIUnit exeUnit, Map<String, HAPDefinitionUICommand> commandsDef, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();
		if(HAPConstant.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
			for(String commandName : commandsDef.keySet()) {
				if(body.getCommandDefinition(commandName)==null) {
					body.addCommandDefinition(commandsDef.get(commandName));
				}
			}
		}
		else {
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(((HAPExecutableUIUnitTag)exeUnit).getTagName())).getContext().getInfo())) {
				escalate(exeUnit.getParent(), commandsDef, uiTagMan);
			}
		}
	}
}
