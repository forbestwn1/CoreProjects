package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorContextRelative;
import com.nosliw.data.core.script.context.HAPProcessorEscalate;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPProcessorUIContext {

	public static void process(HAPExecutableUIUnit uiExe, HAPContextGroup contextDef, HAPContextGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPUITagManager uiTagMan, HAPRequirementContextProcessor contextProcessRequirement){

		HAPConfigureContextProcessor contextProcessorConfig = new HAPConfigureContextProcessor();
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) 	contextProcessorConfig.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;  //for tag, child keeps same
		else contextProcessorConfig.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;   //for resource, parent overwrite child

		processVerticalStructure(uiExe, contextDef, parentContext, contextProcessorConfig, uiTagMan, contextProcessRequirement);		
		processRelativeElement(uiExe, parentContext, contextProcessorConfig, uiTagMan, contextProcessRequirement);
		markProcessed(uiExe);
		discoverVariable(uiExe, contextProcessRequirement.inheritanceExcludedInfo);
		processInteractionElement(uiExe, parentContext, serviceProviders, contextProcessorConfig, uiTagMan, contextProcessRequirement);
	}
	
	private static void markProcessed(HAPExecutableUIUnit uiExe) {
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.getTagContext().processed();
		}

		uiExe.getContext().processed();
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			markProcessed(childTag);			
		}
	}
	
	//process context information
	private static void processVerticalStructure(HAPExecutableUIUnit uiExe, HAPContextGroup contextDef, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPUITagManager uiTagMan, HAPRequirementContextProcessor contextProcessRequirement){
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			//for custom tag, build context for tag first : merge parent context with context definition in tag definition first
			parentContext = buildUITagContext(uiTagExe, parentContext, contextProcessorConfig, uiTagMan, contextProcessRequirement);
			uiTagExe.setTagContext(parentContext);
		}
		
		//merge with context defined in tag unit
		HAPContextGroup extContextGroup = HAPProcessorContext.processStatic(contextDef, parentContext, new HAPConfigureContextProcessor(), contextProcessRequirement);
		uiExe.setContext(extContextGroup);

		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			processEscalate((HAPExecutableUIUnitTag)uiExe, uiTagMan, contextProcessRequirement.inheritanceExcludedInfo);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			processVerticalStructure(childTag, childTag.getUIUnitDefinition().getContextDefinition(), uiExe.getContext(), contextProcessorConfig, uiTagMan, contextProcessRequirement);			
		}
	}
	
	private static void processEscalate(HAPExecutableUIUnitTag exeUITag, HAPUITagManager uiTagMan, Set<String> inheritanceExcludedInfo) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstant.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPProcessorEscalate.process(exeUITag.getContext(), categarys, contextMapping, inheritanceExcludedInfo);
		}
	}
	
	private static void processRelativeElement(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPUITagManager uiTagMan, HAPRequirementContextProcessor contextProcessRequirement){
		HAPConfigureContextProcessor privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPContextGroup.getAllContextTypes();
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;

			//process relative element in tag context
			uiTagExe.setTagContext(HAPProcessorContext.processRelative(uiTagExe.getTagContext(), parentContext, contextProcessorConfig, contextProcessRequirement));
			parentContext = uiTagExe.getTagContext();
			
			//process relative element in event definition in tag
			for(HAPDefinitionUIEvent eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
				HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
				eventDef.cloneToBase(processedEventDef);
				processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventDef.getDataDefinition(), uiTagExe.getTagContext(), privateConfigure, contextProcessRequirement));
				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
			}
		}

		//merge with context defined in resource
		uiExe.setContext(HAPProcessorContext.processRelative(uiExe.getContext(), parentContext, contextProcessorConfig, contextProcessRequirement));

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			processRelativeElement(childTag, uiExe.getContext(), contextProcessorConfig, uiTagMan, contextProcessRequirement);			
		}
		
	}	
	
	private static void processInteractionElement(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, Map<String, HAPDefinitionServiceProvider> serviceProviders, HAPConfigureContextProcessor contextProcessorConfig, HAPUITagManager uiTagMan, HAPRequirementContextProcessor contextProcessRequirement){
		HAPConfigureContextProcessor privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPContextGroup.getAllContextTypes();

		//process relative element in event defined in resource
		Map<String, HAPDefinitionUIEvent> eventsDef = uiExe.getUIUnitDefinition().getEventDefinitions();
		for(String name : eventsDef.keySet()) {
			HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
			eventsDef.get(name).cloneToBase(processedEventDef);
			processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventsDef.get(name).getDataDefinition(), uiExe.getContext(), privateConfigure, contextProcessRequirement));
			uiExe.addEventDefinition(processedEventDef);
		}

		//process relative element in command defined in resource
		Map<String, HAPDefinitionUICommand> commandsDef = uiExe.getUIUnitDefinition().getCommandDefinition();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			//command parms
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), uiExe.getContext(), privateConfigure, contextProcessRequirement));

			//command results
			Map<String, HAPContext> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), uiExe.getContext(), privateConfigure, contextProcessRequirement));
			}
			
			uiExe.addCommandDefinition(processedCommendDef);
		}

		
		//process service
		//all provider available
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		allServiceProviders.putAll(serviceProviders);
		allServiceProviders.putAll(uiExe.getUIUnitDefinition().getServiceProviderDefinitions());
		//make sure all provider has interface info
		for(String name : allServiceProviders.keySet()) {
			HAPDefinitionServiceProvider provider = allServiceProviders.get(name);
			if(provider.getServiceInterface()==null) {
				provider.setServiceInterface(contextProcessRequirement.serviceDefinitionManager.getDefinition(provider.getServiceId()).getStaticInfo().getInterface());
			}
		}
		
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = uiExe.getUIUnitDefinition().getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			HAPExecutableServiceUse serviceUseExe = HAPProcessorServiceUse.process(serviceUseDef, serviceProvider.getServiceInterface(), uiExe.getFlatContext().getContext(), contextProcessorConfig, contextProcessRequirement);	
			uiExe.addServiceDefinition(serviceName, serviceUseExe);
			uiExe.addServiceProvider(serviceUseDef.getProvider(), serviceProvider);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			processInteractionElement(childTag, uiExe.getContext(), allServiceProviders, contextProcessorConfig, uiTagMan, contextProcessRequirement);			
		}
	}
	
	private static void discoverVariable(HAPExecutableUIUnit uiExe, Set<String> inheritanceExcludedInfo){
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			//flat it
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setFlatTagContext(HAPUtilityContext.buildFlatContextFromContextGroup(uiTagExe.getTagContext(), inheritanceExcludedInfo));
		}
		
		//build flat context
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContextFromContextGroup(uiExe.getContext(), inheritanceExcludedInfo);
		uiExe.setFlatContext(flatContext);

		//constants
		Map<String, Object> constantsValue = flatContext.getConstantValue();
		for(String name : constantsValue.keySet()) {
			Object constantValue = constantsValue.get(name);
			uiExe.addConstantValue(name, constantValue);
		}
		
		//build variables
		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(flatContext.getContext());
		for(String varName : varsInfo.keySet()) {
			uiExe.getExpressionContext().addDataVariable(varName, varsInfo.get(varName));
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			discoverVariable(childTag, inheritanceExcludedInfo);			
		}
	}
	
	
	//build context for ui Tag
	private static HAPContextGroup buildUITagContext(HAPExecutableUIUnitTag uiTag, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPUITagManager uiTagMan, HAPRequirementContextProcessor contextProcessRequirement){
		//get contextDef 
		HAPUITagDefinitionContext tagDefinitionContext = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getUIUnitTagDefinition().getTagName())).getContext();

		//add attribute constant as part of tagContext
		Map<String, String> constants = uiTag.getAttributes();
		HAPContextGroup tagContext = tagDefinitionContext.cloneContextGroup();
		for(String name : constants.keySet()) {
			HAPContextDefinitionLeafConstant cstRootNode = new HAPContextDefinitionLeafConstant(constants.get(name));
			tagContext.addElement(HAPConstant.NOSLIW_RESERVE_ATTRIBUTE + name, new HAPContextDefinitionRoot(cstRootNode), HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		
		HAPConfigureContextProcessor configure = contextProcessorConfig.cloneConfigure();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefinitionContext.getInfo());
		HAPContextGroup out = HAPProcessorContext.processStatic(tagContext, parentContext, configure, contextProcessRequirement);
		return out;
	}
}
