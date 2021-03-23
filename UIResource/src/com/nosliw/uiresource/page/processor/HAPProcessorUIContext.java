package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorContextRelative;
import com.nosliw.data.core.script.context.HAPProcessorEscalate;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIContext {

	//expand context reference by using context definition in attachment
	public static void expandContextReference(HAPDefinitionUIUnit uiUnitDef, HAPManagerResourceDefinition resourceDefMan) {
		HAPUtilityComponent.resolveContextReference(uiUnitDef.getContextStructure(), uiUnitDef.getContextReferences(), uiUnitDef.getAttachmentContainer(), resourceDefMan);
		
		//process child tags
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			expandContextReference(uiTag, resourceDefMan);
		}
	}
	
	public static void enhanceContextByService(HAPDefinitionUIUnit uiUnitDef, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : uiUnitDef.getAllServices()) {
			HAPDefinitionServiceUse service = uiUnitDef.getService(serviceName);
			HAPProcessorServiceUse.enhanceContextByService(service, uiUnitDef.getContextStructure(), runtimeEnv);
		}
		
		//child tag
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			enhanceContextByService(uiTag, runtimeEnv);
		}
	}
	
	public static void process(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIBody body = uiExe.getBody();
		HAPConfigureContextProcessor contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 

		HAPConfigureContextProcessor privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPContextGroup.getAllContextTypes();

		//merge with parent
		mergeContext(uiExe, parentContext, contextProcessorConfig, uiTagMan, runtimeEnv);		

		processRelativeElement(uiExe, parentContext, privateConfigure, uiTagMan, runtimeEnv);
		markProcessed(uiExe);
		flatenContext(uiExe, privateConfigure.inheritanceExcludedInfo);
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
	private static void mergeContext(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPConfigureContextProcessor contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPContextGroup contextDef = uiExe.getUIUnitDefinition().getContextNotFlat();
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
			mergeContext(childTag, uiExe.getBody().getContext(), contextProcessorConfig, uiTagMan, runtimeEnv);			
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
	
	private static void flatenContext(HAPExecutableUIUnit uiExe, Set<String> inheritanceExcludedInfo){
		
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			//flat it
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setFlatTagContext(HAPUtilityContext.buildFlatContextFromContextGroup(uiTagExe.getTagContext(), inheritanceExcludedInfo));
		}
		
		//build flat context
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContextFromContextGroup(uiExe.getBody().getContext(), inheritanceExcludedInfo);
		uiExe.getBody().setFlatContext(flatContext);

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			flatenContext(childTag, inheritanceExcludedInfo);			
		}
	}
}
