package com.nosliw.uiresource.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorContextRelative;
import com.nosliw.data.core.script.context.HAPProcessorEscalate;
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
		markProcessed(uiExe);
		process3(uiExe, contextProcessorEnv.inheritanceExcludedInfo);
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
	private static void process1(HAPExecutableUIUnit uiExe, HAPExecutableUIUnit parentUIExe, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
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
			processEscalate((HAPExecutableUIUnitTag)uiExe, uiTagMan, contextProcessorEnv.inheritanceExcludedInfo);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			process1(childTag, uiExe, uiTagMan, contextProcessorEnv);			
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
	
	private static void processRelative(HAPExecutableUIUnit uiExe, HAPExecutableUIUnit parentUIExe, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextGroup parentContext = parentUIExe==null?null:parentUIExe.getContext();
		
		HAPConfigureContextProcessor privateConfigure = new HAPConfigureContextProcessor();
		privateConfigure.parentCategary = HAPContextGroup.getAllContextTypes();
		
		if(uiExe.getType().equals(HAPConstant.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;

			//process relative element in tag context
			uiTagExe.setTagContext(HAPProcessorContext.processRelative(uiTagExe.getTagContext(), parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv));
			parentContext = uiTagExe.getTagContext();
			
			//process relative element in event definition in tag
			for(HAPDefinitionUIEvent eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
				HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
				eventDef.cloneToBase(processedEventDef);
				processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventDef.getDataDefinition(), uiTagExe.getTagContext(), privateConfigure, contextProcessorEnv));
				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
			}
		}

		//merge with context defined in resource
		uiExe.setContext(HAPProcessorContext.processRelative(uiExe.getContext(), parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv));

		//process relative element in event defined in resource
		Map<String, HAPDefinitionUIEvent> eventsDef = uiExe.getUIUnitDefinition().getEventDefinitions();
		for(String name : eventsDef.keySet()) {
			HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
			eventsDef.get(name).cloneToBase(processedEventDef);
			processedEventDef.setDataDefinition(HAPProcessorContextRelative.process(eventsDef.get(name).getDataDefinition(), uiExe.getContext(), privateConfigure, contextProcessorEnv));
			uiExe.addEventDefinition(processedEventDef);
		}

		//process relative element in command defined in resource
		Map<String, HAPDefinitionUICommand> commandsDef = uiExe.getUIUnitDefinition().getCommandDefinition();
		for(String name : commandsDef.keySet()) {
			HAPDefinitionUICommand commandDef = commandsDef.get(name);
			HAPDefinitionUICommand processedCommendDef = new HAPDefinitionUICommand();
			commandDef.cloneBasicTo(processedCommendDef);
			//command parms
			processedCommendDef.setParms(HAPProcessorContextRelative.process(commandDef.getParms(), uiExe.getContext(), privateConfigure, contextProcessorEnv));

			//command results
			Map<String, HAPContext> results = commandDef.getResults();
			for(String resultName : results.keySet()) {
				processedCommendDef.addResult(resultName, HAPProcessorContextRelative.process(results.get(resultName), uiExe.getContext(), privateConfigure, contextProcessorEnv));
			}
			
			uiExe.addCommandDefinition(processedCommendDef);
		}
		
		//process service defined in resource
		Map<String, HAPDefinitionUICommand> servicesDef = uiExe.getUIUnitDefinition().getServiceDefinitions();
		for(String name : servicesDef.keySet()) {
			HAPDefinitionUICommand serviceDef = servicesDef.get(name);
			HAPDefinitionServiceInfo processedServiceDef = new HAPDefinitionServiceInfo();
			
			serviceDef.cloneToEntityInfo(processedServiceDef);
			//process parms and convert context root to data type criteria required by service parm
			HAPContext processedParmContext = HAPProcessorContextRelative.process(serviceDef.getParms(), uiExe.getContext(), privateConfigure, contextProcessorEnv);
			for(String parmName : processedParmContext.getElementNames()) {
				HAPContextDefinitionRoot contextParm = processedParmContext.getElement(parmName);
				HAPDefinitionServiceParm serviceParm = new HAPDefinitionServiceParm(); 
				contextParm.cloneToEntityInfo(serviceParm);
				
				HAPContextDefinitionElement solidContextParmEle = contextParm.getDefinition().getSolidContextDefinitionElement();
				String contextType = solidContextParmEle.getType();
				switch(contextType) {
				case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
					HAPContextDefinitionLeafData dataRootNode = (HAPContextDefinitionLeafData)solidContextParmEle;
					serviceParm.setCriteria(dataRootNode.getCriteria().getCriteria());
					break;
				case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
					HAPContextDefinitionLeafConstant constantRootNode = (HAPContextDefinitionLeafConstant)solidContextParmEle;
					serviceParm.setDefault(constantRootNode.getDataValue());
					break;
				default:
					//invalid
					HAPErrorUtility.invalid("");
				}
				processedServiceDef.getInterface().addParm(parmName, serviceParm);
			}
			
			//process results and convert context root to data type criteria required by service result
			Map<String, HAPContext> resultsContext = serviceDef.getResults();
			for(String resultName : resultsContext.keySet()) {
				HAPContext processedResultContext = HAPProcessorContextRelative.process(resultsContext.get(resultName), uiExe.getContext(), privateConfigure, contextProcessorEnv);
				HAPDefinitionServiceResult result = new HAPDefinitionServiceResult();
				for(String outputName : processedResultContext.getElementNames()) {
					HAPContextDefinitionRoot contextOutput = processedResultContext.getElement(outputName);
					HAPDefinitionServiceOutput serviceOutput = new HAPDefinitionServiceOutput(); 
					contextOutput.cloneToEntityInfo(serviceOutput);
					
					HAPContextDefinitionElement solidContextOutputEle = contextOutput.getDefinition().getSolidContextDefinitionElement();
					String contextType = solidContextOutputEle.getType();
					switch(contextType) {
					case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
						HAPContextDefinitionLeafData dataRootNode = (HAPContextDefinitionLeafData)solidContextOutputEle;
						serviceOutput.setCriteria(dataRootNode.getCriteria().getCriteria());
						break;
					case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
						break;
					default:
						//invalid
						HAPErrorUtility.invalid("");
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
	
	private static void process3(HAPExecutableUIUnit uiExe, Set<String> inheritanceExcludedInfo){
		
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
			uiExe.getExpressionContext().addVariable(varName, varsInfo.get(varName));
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getUITags()) {
			process3(childTag, inheritanceExcludedInfo);			
		}
	}
	
	
	//build context for ui Tag
	private static HAPContextGroup buildUITagContext(HAPExecutableUIUnitTag uiTag, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		//get contextDef 
		HAPUITagDefinitionContext tagDefinitionContext = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getUIUnitTagDefinition().getTagName())).getContext();

		//add attribute constant as part of tagContext
		Map<String, String> constants = uiTag.getAttributes();
		HAPContextGroup tagContext = tagDefinitionContext.cloneContextGroup();
		for(String name : constants.keySet()) {
			HAPContextDefinitionLeafConstant cstRootNode = new HAPContextDefinitionLeafConstant(constants.get(name));
			tagContext.addElement(HAPConstant.NOSLIW_RESERVE_ATTRIBUTE + name, new HAPContextDefinitionRoot(cstRootNode), HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(tagDefinitionContext.getInfo());
		HAPContextGroup out = HAPProcessorContext.process1(tagContext, parentContext, configure, contextProcessorEnv);
		return out;
	}
}
