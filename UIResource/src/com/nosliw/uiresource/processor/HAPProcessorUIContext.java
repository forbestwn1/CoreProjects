package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafVariable;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextNodeRoot;
import com.nosliw.data.core.script.context.HAPContextNodeRootConstant;
import com.nosliw.data.core.script.context.HAPContextNodeRootVariable;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorContextRelative;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.HAPDefinitionServiceInfo;
import com.nosliw.data.core.service.HAPDefinitionServiceOutput;
import com.nosliw.data.core.service.HAPDefinitionServiceParm;
import com.nosliw.data.core.service.HAPDefinitionServiceResult;
import com.nosliw.uiresource.page.definition.HAPDefinitionUICommand;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPProcessorUIContext {

	public static void process(HAPExecutableUIUnit uiExe, HAPExecutableUIUnit parentUIExe, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		process1(uiExe, parentUIExe, uiTagMan, contextProcessorEnv);			
		processRelative(uiExe, parentUIExe, uiTagMan, contextProcessorEnv);			
		process3(uiExe);
	}
	
	//process context information
	public static void process1(HAPExecutableUIUnit uiExe, HAPExecutableUIUnit parentUIExe, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextGroup parentContext = parentUIExe==null?null:parentUIExe.getContext();
		
		HAPContextGroup contextDef = uiExe.getUIUnitDefinition().getContextDefinition();

		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			//for custom tag, build context for tag first : merge parent context with context definition in tag definition first
			parentContext = buildUITagContext(uiTagExe, parentContext, uiTagMan, contextProcessorEnv);
			uiTagExe.setTagContext(parentContext);
		}
		
		//merge with context defined in tag unit
		HAPContextGroup extContextGroup = HAPProcessorContext.process1(contextDef, parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv);
		uiExe.setContext(extContextGroup);

		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			processEscalate((HAPExecutableUIUnitTag)uiExe, uiTagMan);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			process1(childTag, uiExe, uiTagMan, contextProcessorEnv);			
		}
	}

	private static void processEscalate(HAPExecutableUIUnitTag exeUITag, HAPUITagManager uiTagMan) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getContext())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstant.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPUtilityContext.processEscalate(exeUITag.getContext(), categarys, contextMapping);
		}
	}
	
	private static void processRelative(HAPExecutableUIUnit uiExe, HAPExecutableUIUnit parentUIExe, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextGroup parentContext = parentUIExe==null?null:parentUIExe.getContext();
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			
			HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
			uiTagExe.setTagContext(HAPProcessorContext.process2(uiTagExe.getTagContext(), parentContext, configure, contextProcessorEnv));
			parentContext = uiTagExe.getTagContext();
			
			//process event in tag
			for(HAPContextEntity eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
				HAPContextEntity processedEventDef = new HAPContextEntity();
				eventDef.cloneBasicTo(processedEventDef);
				processedEventDef.setContext(HAPProcessorContextRelative.process(eventDef.getContext(), uiTagExe.getTagContext(), null, contextProcessorEnv));
				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
			}
			
		}

		//merge with context defined in resource
		uiExe.setContext(HAPProcessorContext.process2(uiExe.getContext(), parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv));

		//process event defined in resoruce
		Map<String, HAPDefinitionUIEvent> eventsDef = uiExe.getUIUnitDefinition().getEventDefinitions();
		for(String name : eventsDef.keySet()) {
			HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
			eventsDef.get(name).cloneBasicTo(processedEventDef);
			processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventsDef.get(name).getDataDefinition(), uiExe.getContext(), null, contextProcessorEnv));
			uiExe.addEventDefinition(processedEventDef);
		}

		//process command defined in resource
		Map<String, HAPDefinitionUICommand> commandsDef = uiExe.getUIUnitDefinition().getCommandDefinition();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), uiExe.getContext(), null, contextProcessorEnv));
			
			Map<String, HAPContext> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), uiExe.getContext(), null, contextProcessorEnv));
			}
			
			uiExe.addCommandDefinition(processedCommendDef);
		}
		
		//process service defined in resource
		Map<String, HAPDefinitionUICommand> servicesDef = uiExe.getUIUnitDefinition().getCommandDefinition();
		for(String name : servicesDef.keySet()) {
			HAPDefinitionUICommand serviceDef = servicesDef.get(name);
			HAPDefinitionServiceInfo processedServiceDef = new HAPDefinitionServiceInfo();
			
			serviceDef.cloneToEntityInfo(processedServiceDef);
			HAPContext processedParmContext = HAPProcessorContextRelative.process(serviceDef.getParms(), uiExe.getContext(), null, contextProcessorEnv);
			for(String parmName : processedParmContext.getElementNames()) {
				HAPContextDefinitionRoot contextParm = processedParmContext.getElement(parmName);
				HAPDefinitionServiceParm serviceParm = new HAPDefinitionServiceParm(); 
				contextParm.cloneToEntityInfo(serviceParm);
				
				String contextType = contextParm.getDefinition().getType();
				switch(contextType) {
				case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
					HAPContextDefinitionLeafRelative relativeRootNode = (HAPContextDefinitionLeafRelative)contextParm.getDefinition();
//					serviceParm.setCriteria(relativeRootNode.getDefinition().g.getDefinition().getValue());
					break;
				case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
					HAPContextDefinitionLeafData dataRootNode = (HAPContextDefinitionLeafData)contextParm.getDefinition();
					serviceParm.setCriteria(dataRootNode.getCriteria().getCriteria());
					break;
				case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
					HAPContextDefinitionLeafConstant constantRootNode = (HAPContextDefinitionLeafConstant)contextParm.getDefinition();
					serviceParm.setDefault(constantRootNode.getDataValue());
					break;
				}
				processedServiceDef.getInterface().addParm(parmName, serviceParm);
			}
			
			Map<String, HAPContext> resultsContext = serviceDef.getResults();
			for(String resultName : resultsContext.keySet()) {
				HAPContext processedResultContext = HAPProcessorContextRelative.process(resultsContext.get(resultName), uiExe.getContext(), null, contextProcessorEnv);
				HAPDefinitionServiceResult result = new HAPDefinitionServiceResult();
				for(String outputName : processedResultContext.getElementNames()) {
					HAPContextDefinitionRoot contextOutput = processedResultContext.getElement(outputName);
					HAPDefinitionServiceOutput serviceOutput = new HAPDefinitionServiceOutput(); 
					contextOutput.cloneToEntityInfo(serviceOutput);
					
					String contextType = contextOutput.getDefinition().getType();
					switch(contextType) {
					case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
						HAPContextDefinitionLeafRelative relativeRootNode = (HAPContextDefinitionLeafRelative)contextOutput.getDefinition();
//						serviceOutput.setCriteria(relativeRootNode.getDefinition().getValue());
						break;
					case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
						HAPContextDefinitionLeafData dataRootNode = (HAPContextDefinitionLeafData)contextOutput.getDefinition();
						serviceOutput.setCriteria(dataRootNode.getCriteria().getCriteria());
						break;
					case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
						break;
					}
					result.addOutput(outputName, serviceOutput);
					processedServiceDef.getInterface().addResult(resultName, result);
				}
				
			}
			uiExe.addServiceDefinition(processedServiceDef);
		}
		
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			processRelative(childTag, uiExe, uiTagMan, contextProcessorEnv);			
		}
		
	}	
	
	private static void process3(HAPExecutableUIUnit uiExe){
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			//flat it
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setFlatTagContext(HAPUtilityContext.buildFlatContext(uiTagExe.getTagContext()));
		}
		
		//build flat context
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContext(uiExe.getContext());
		uiExe.setFlatContext(flatContext);

		Map<String, Object> constantsValue = flatContext.getConstantValue();
		for(String name : constantsValue.keySet()) {
			Object constantValue = constantsValue.get(name);
			uiExe.addConstantValue(name, constantValue);
		}
		
		//build variables
		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(flatContext.getContext());
		for(String varName : varsInfo.keySet()) {
			uiExe.getExpressionContext().addVariable(varName, varsInfo.get(varName));
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			process3(childTag);			
		}
	}
	
	
	//build context for ui Tag
	private static HAPContextGroup buildUITagContext(HAPExecutableUIUnitTag uiTag, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		//get contextDef 
		HAPUITagDefinitionContext tagDefinitionContext = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getUIUnitTagDefinition().getTagName())).getContext();

		//add attribute constant as part of tagContext
		Map<String, String> constants = uiTag.getAttributes();
		HAPContextGroup tagContext = tagDefinitionContext.clone();
		for(String cstName : constants.keySet()) {
			HAPContextDefinitionLeafConstant cstRootNode = new HAPContextDefinitionLeafConstant(constants.get(cstName));
			tagContext.addElement(cstName, new HAPContextDefinitionRoot(cstRootNode), HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefinitionContext);
		HAPContextGroup out = HAPProcessorContext.process1(tagContext, parentContext, configure, contextProcessorEnv);
		return out;
	}
}
