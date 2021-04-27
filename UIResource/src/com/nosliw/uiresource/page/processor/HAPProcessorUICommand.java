package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPProcessorContextRelative;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUICommand {

	private static void processInteractionElement(HAPExecutableUIUnit uiExe, HAPContextStructureValueDefinitionGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		//process relative element in event defined in resource
		Map<String, HAPDefinitionUIEvent> eventsDef = uiUnitDef.getEventDefinitions();
		for(String name : eventsDef.keySet()) {
			HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
			eventsDef.get(name).cloneToBase(processedEventDef);
			processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventsDef.get(name).getDataDefinition(), HAPParentContext.createDefault(uiExe.getBody().getContext()), null, contextProcessorConfig, runtimeEnv));
			uiExe.getBody().addEventDefinition(processedEventDef);
		}

		//process relative element in command defined in resource
		Map<String, HAPDefinitionUICommand> commandsDef = uiUnitDef.getCommandDefinition();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			//command parms
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), HAPParentContext.createDefault(uiExe.getBody().getContext()), null, contextProcessorConfig, runtimeEnv));

			//command results
			Map<String, HAPContextStructureValueDefinitionFlat> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), HAPParentContext.createDefault(uiExe.getBody().getContext()), null, contextProcessorConfig, runtimeEnv));
			}
			
			uiExe.getBody().addCommandDefinition(processedCommendDef);
		}

		
		//process service
		//all provider available
//		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(serviceProviders, uiExe.getUIUnitDefinition(), contextProcessRequirement.serviceDefinitionManager); 
				
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processInteractionElement(childTag, uiExe.getBody().getContext(), null, contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}
	
	public static void processCommand(HAPExecutableUIUnit uiExe, HAPRuntimeEnvironment runtimeEnv) {
		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 
		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		//process relative element in command defined in resource
		Map<String, HAPDefinitionUICommand> commandsDef = uiUnitDef.getCommandDefinition();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			//command parms
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), HAPParentContext.createDefault(uiExe.getBody().getContext()), null, contextProcessorConfig, runtimeEnv));

			//command results
			Map<String, HAPContextStructureValueDefinitionFlat> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), HAPParentContext.createDefault(uiExe.getBody().getContext()), null, contextProcessorConfig, runtimeEnv));
			}
			
			uiExe.getBody().addCommandDefinition(processedCommendDef);
		}
		
	}

	public static void escalateCommand(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();

		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
				Map<String, HAPDefinitionUICommand> mappedCommandDefs = new LinkedHashMap<String, HAPDefinitionUICommand>();
				
				Map<String, String> nameMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_COMMAND));
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
			escalateCommand(childTag, uiTagMan);
		}
	}
	
	private static void escalate(HAPExecutableUIUnit exeUnit, Map<String, HAPDefinitionUICommand> commandsDef, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
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
