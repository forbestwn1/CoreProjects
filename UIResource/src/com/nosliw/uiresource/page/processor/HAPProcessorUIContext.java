package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorContextRelative;
import com.nosliw.data.core.script.context.HAPProcessorEscalate;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIContext {

	public static void process(HAPExecutableUIUnit uiExe, HAPContextGroup contextDef, HAPContextGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIBody body = uiExe.getBody();
		HAPConfigureContextProcessor contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 

		HAPConfigureContextProcessor privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPContextGroup.getAllContextTypes();

		//merge with parent
		processVerticalStructure(uiExe, contextDef, parentContext, contextProcessorConfig, uiTagMan, runtimeEnv);		

		processService(uiExe, runtimeEnv);
		processRelativeElement(uiExe, parentContext, privateConfigure, uiTagMan, runtimeEnv);
		markProcessed(uiExe);
		discoverVariable(uiExe, privateConfigure.inheritanceExcludedInfo);
		processInteractionElement(uiExe, parentContext, serviceProviders, privateConfigure, uiTagMan, runtimeEnv);
	}
	
	private static void processService(HAPExecutableUIUnit uiExe, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionUIUnit uiUnitDef = uiExe.getUIUnitDefinition();
		for(String serviceName : uiUnitDef.getAllServices()) {
			HAPDefinitionServiceUse service = uiUnitDef.getService(serviceName);
			HAPExecutableServiceUse serviceExe = HAPProcessorServiceUse.process(service, uiExe.getBody().getContext(), uiUnitDef.getAttachmentContainer(), runtimeEnv);
			uiExe.getBody().addServiceUse(serviceName, serviceExe);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processService(childTag, runtimeEnv);			
		}
	}
	
	private static void markProcessed(HAPExecutableUIUnit uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.getTagContext().processed();
		}

		uiExe.getBody().getContext().processed();
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			markProcessed(childTag);			
		}
	}
	
	//process context information
	private static void processVerticalStructure(HAPExecutableUIUnit uiExe, HAPContextGroup contextDef, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			//for custom tag, build context for tag first : merge parent context with context definition in tag definition first
			parentContext = HAPUtilityProcess.buildUITagContext(uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())), parentContext, uiTagExe.getAttributes(), contextProcessorConfig, runtimeEnv);
			uiTagExe.setTagContext(parentContext);
		}
		
		//merge with context defined in tag unit
		HAPContextGroup extContextGroup = HAPProcessorContext.processStatic(contextDef, HAPParentContext.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv);
		uiExe.getBody().setContext(extContextGroup);

		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			processEscalate((HAPExecutableUIUnitTag)uiExe, uiTagMan, contextProcessorConfig.inheritanceExcludedInfo);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processVerticalStructure(childTag, childTag.getUIUnitDefinition().getContextNotFlat(), uiExe.getBody().getContext(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}
	
	private static void processEscalate(HAPExecutableUIUnitTag exeUITag, HAPManagerUITag uiTagMan, Set<String> inheritanceExcludedInfo) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstantShared.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPProcessorEscalate.process(exeUITag.getBody().getContext(), categarys, contextMapping, inheritanceExcludedInfo);
		}
	}
	
	private static void processRelativeElement(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;

			//process relative element in tag context
			uiTagExe.setTagContext(HAPProcessorContext.processRelative(uiTagExe.getTagContext(), HAPParentContext.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv));
			parentContext = uiTagExe.getTagContext();
			
			//process relative element in event definition in tag
			for(HAPDefinitionUIEvent eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
				HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
				eventDef.cloneToBase(processedEventDef);
				processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventDef.getDataDefinition(), HAPParentContext.createDefault(uiTagExe.getTagContext()), null, contextProcessorConfig, runtimeEnv));
				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
			}
		}

		//merge with context defined in resource
		uiExe.getBody().setContext(HAPProcessorContext.processRelative(uiExe.getBody().getContext(), HAPParentContext.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv));

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processRelativeElement(childTag, uiExe.getBody().getContext(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
		
	}	
	
	private static void processInteractionElement(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPConfigureContextProcessor contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
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
			Map<String, HAPContext> results = commandDef.getResults();
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
	
	private static void discoverVariable(HAPExecutableUIUnit uiExe, Set<String> inheritanceExcludedInfo){
		
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			//flat it
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setFlatTagContext(HAPUtilityContext.buildFlatContextFromContextGroup(uiTagExe.getTagContext(), inheritanceExcludedInfo));
		}
		
		//build flat context
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContextFromContextGroup(uiExe.getBody().getContext(), inheritanceExcludedInfo);
		uiExe.getBody().setFlatContext(flatContext);

		//constants
//		Map<String, Object> constantsValue = flatContext.getConstantValue();
//		for(String name : constantsValue.keySet()) {
//			Object constantValue = constantsValue.get(name);
//			uiExe.addConstantValue(name, constantValue);
//		}
		
		//build variables
//		uiExe.getExpressionSuiteInContext().setContextStructure(flatContext.getContext());
//		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(flatContext.getContext());
//		for(String varName : varsInfo.keySet()) {
//			uiExe.getExpressionContext().addDataVariable(varName, varsInfo.get(varName));
//		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			discoverVariable(childTag, inheritanceExcludedInfo);			
		}
	}
	
	
}
