package com.nosliw.uiresource.page.processor;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.command.HAPExecutableCommand;
import com.nosliw.data.core.component.command.HAPProcessorCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.temp.HAPProcessorContextRelative;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUICommand {

	public static void processCommand(HAPExecutableUIUnit uiExe, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		//process relative element in command defined in resource
		List<HAPDefinitionCommand> commandsDef = uiUnitDef.getCommands();
		for(HAPDefinitionCommand commandDef : commandsDef) {
			HAPExecutableCommand commandExe = HAPProcessorCommand.process(commandDef.cloneCommandDefinition(), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), runtimeEnv);
			uiExe.getBody().addCommand(commandExe);
		}

		
	}

	public static void escalateCommand(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
//		HAPExecutableUIBody body = exeUnit.getBody();
//
//		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
//			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
//			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
//				Map<String, HAPDefinitionUICommand> mappedCommandDefs = new LinkedHashMap<String, HAPDefinitionUICommand>();
//				
//				Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_COMMAND));
//				exeTag.setCommandMapping(nameMapping);
//				Map<String, HAPDefinitionUICommand> exeCommandDefs = body.getCommandDefinitions();
//				for(String commandName : exeCommandDefs.keySet()) {
//					String mappedName = nameMapping.get(commandName);
//					if(mappedName==null)   mappedName = commandName;
//					mappedCommandDefs.put(mappedName, exeCommandDefs.get(commandName));
//				}
//				escalate(exeTag.getParent(), mappedCommandDefs, uiTagMan);
//			}
//		}
//
//		//child tag
//		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
//			escalateCommand(childTag, uiTagMan);
//		}
	}

	
	private static void processInteractionElement(HAPExecutableUIUnit uiExe, HAPValueStructureDefinitionGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		//process relative element in event defined in resource
		List<HAPDefinitionEvent> eventsDef = uiUnitDef.getEvents();
		for(HAPDefinitionEvent eventDef : eventsDef) {
			HAPDefinitionEvent processedEventDef = eventDef.cloneEventDefinition();
			processedEventDef.setValueMapping(HAPProcessorContextRelative.process(eventDef.getValueMapping(), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), null, contextProcessorConfig, runtimeEnv));
			uiExe.getBody().addEventDefinition(processedEventDef);
		}

		//process relative element in command defined in resource
		List<HAPDefinitionCommand> commandsDef = uiUnitDef.getCommands();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			//command parms
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), null, contextProcessorConfig, runtimeEnv));

			//command results
			Map<String, HAPValueStructureDefinitionFlat> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), null, contextProcessorConfig, runtimeEnv));
			}
			
			uiExe.getBody().addCommand(processedCommendDef);
		}

		
		//process service
		//all provider available
//		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(serviceProviders, uiExe.getUIUnitDefinition(), contextProcessRequirement.serviceDefinitionManager); 
				
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processInteractionElement(childTag, (HAPValueStructureDefinitionGroup)uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure(), null, contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}
	
	public static void processCommand1(HAPExecutableUIUnit uiExe, HAPRuntimeEnvironment runtimeEnv) {
//		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 
//		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
//		//process relative element in command defined in resource
//		Map<String, HAPDefinitionUICommand> commandsDef = uiUnitDef.getCommandDefinition();
//		for(String name : commandsDef.keySet()) {
//			HAPDefinitionUICommand commandDef = commandsDef.get(name);
//			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
//			commandDef.cloneBasicTo(processedCommendDef);
//			//command parms
//			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), null, contextProcessorConfig, runtimeEnv));
//
//			//command results
//			Map<String, HAPValueStructureDefinitionFlat> results = commandDef.getResults();
//			for(String resultName : results.keySet()) {
//				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), HAPContainerStructure.createDefault(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()), null, contextProcessorConfig, runtimeEnv));
//			}
//			
//			uiExe.getBody().addCommandDefinition(processedCommendDef);
//		}
		
	}

	public static void escalateCommand1(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
//		HAPExecutableUIBody body = exeUnit.getBody();
//
//		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
//			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
//			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
//				Map<String, HAPDefinitionUICommand> mappedCommandDefs = new LinkedHashMap<String, HAPDefinitionUICommand>();
//				
//				Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_COMMAND));
//				exeTag.setCommandMapping(nameMapping);
//				Map<String, HAPDefinitionUICommand> exeCommandDefs = body.getCommandDefinitions();
//				for(String commandName : exeCommandDefs.keySet()) {
//					String mappedName = nameMapping.get(commandName);
//					if(mappedName==null)   mappedName = commandName;
//					mappedCommandDefs.put(mappedName, exeCommandDefs.get(commandName));
//				}
//				escalate(exeTag.getParent(), mappedCommandDefs, uiTagMan);
//			}
//		}
//
//		//child tag
//		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
//			escalateCommand(childTag, uiTagMan);
//		}
	}
	
	private static void escalate(HAPExecutableUIUnit exeUnit, Map<String, HAPDefinitionUICommand> commandsDef, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
			for(String commandName : commandsDef.keySet()) {
				if(body.getCommandDefinition(commandName)==null) {
					body.addCommand(commandsDef.get(commandName));
				}
			}
		}
		else {
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(((HAPExecutableUIUnitTag)exeUnit).getTagName())).getValueStructureDefinition().getInfo())) {
				escalate(exeUnit.getParent(), commandsDef, uiTagMan);
			}
		}
	}
}
