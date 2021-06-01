package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPProcessorElementRelative;
import com.nosliw.data.core.structure.HAPProcessorEscalate;
import com.nosliw.data.core.structure.HAPProcessorStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
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
		HAPUtilityComponent.resolveContextReference(uiUnitDef.getValueStructure(), uiUnitDef.getContextReferences(), uiUnitDef.getAttachmentContainer(), resourceDefMan);
		
		//process child tags
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			expandContextReference(uiTag, resourceDefMan);
		}
	}
	
	public static void enhanceContextByService(HAPDefinitionUIUnit uiUnitDef, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : uiUnitDef.getAllServices()) {
			HAPDefinitionServiceUse service = uiUnitDef.getService(serviceName);
			HAPProcessorServiceUse.enhanceContextByService(service, uiUnitDef.getValueStructure(), runtimeEnv);
		}
		
		//child tag
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			enhanceContextByService(uiTag, runtimeEnv);
		}
	}
	
	public static void process(HAPExecutableUIUnit uiExe, HAPValueStructureDefinitionGroup parentContext, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIBody body = uiExe.getBody();
		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiExe.getType()); 

		HAPConfigureProcessorStructure privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPValueStructureDefinitionGroup.getAllCategaries();

		//merge with parent
		mergeContext(uiExe, parentContext, contextProcessorConfig, uiTagMan, runtimeEnv);		

		processRelativeElement(uiExe, parentContext, privateConfigure, uiTagMan, runtimeEnv);
		markProcessed(uiExe);
		flatenContext(uiExe);
	}
	
	private static void markProcessed(HAPExecutableUIUnit uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.getTagValueStructureDefinition().processed();
		}

		uiExe.getBody().getValueStructureDefinition().processed();
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			markProcessed(childTag);			
		}
	}
	
	//process context information
	private static void mergeContext(HAPExecutableUIUnit uiExe, HAPValueStructureDefinitionGroup parentContext, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPValueStructureDefinitionGroup contextDef = uiExe.getUIUnitDefinition().getContextNotFlat();
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			//for custom tag, build context for tag first : merge parent context with context definition in tag definition first
			parentContext = HAPUtilityProcess.buildUITagContext(uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())), parentContext, uiTagExe.getAttributes(), contextProcessorConfig, runtimeEnv);
			uiTagExe.setTagValueStructureDefinition(parentContext);
		}
		
		//merge with context defined in tag unit
		HAPValueStructureDefinitionGroup extContextGroup = (HAPValueStructureDefinitionGroup)HAPProcessorStructure.processStatic(contextDef, HAPContainerStructure.createDefault(parentContext), uiExe.getUIUnitDefinition().getAttachmentContainer(), null, contextProcessorConfig, runtimeEnv);
		uiExe.getBody().setValueStructureDefinition(extContextGroup);

		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			processEscalate((HAPExecutableUIUnitTag)uiExe, uiTagMan, contextProcessorConfig.inheritanceExcludedInfo);
		}
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			mergeContext(childTag, uiExe.getBody().getValueStructureDefinition(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}
	
	private static void processEscalate(HAPExecutableUIUnitTag exeUITag, HAPManagerUITag uiTagMan, Set<String> inheritanceExcludedInfo) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstantShared.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPProcessorEscalate.process(exeUITag.getBody().getValueStructureDefinition(), categarys, contextMapping, inheritanceExcludedInfo);
		}
	}
	
	private static void processRelativeElement(HAPExecutableUIUnit uiExe, HAPValueStructureDefinitionGroup parentContext, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;

			//process relative element in tag context
			uiTagExe.setTagValueStructureDefinition((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(uiTagExe.getTagValueStructureDefinition(), HAPContainerStructure.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv));
			parentContext = uiTagExe.getTagValueStructureDefinition();
			
			//process relative element in event definition in tag
			for(HAPDefinitionUIEvent eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
				HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
				eventDef.cloneToBase(processedEventDef);
				processedEventDef.setDataDefinition((HAPValueStructureDefinitionFlat)HAPProcessorElementRelative.process(eventDef.getDataDefinition(), HAPContainerStructure.createDefault(uiTagExe.getTagValueStructureDefinition()), null, contextProcessorConfig, runtimeEnv));
				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
			}
		}

		//merge with context defined in resource
		uiExe.getBody().setValueStructureDefinition((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(uiExe.getBody().getValueStructureDefinition(), HAPContainerStructure.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv));

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processRelativeElement(childTag, uiExe.getBody().getValueStructureDefinition(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
		
	}	
	
	private static void flatenContext(HAPExecutableUIUnit uiExe){
		
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			//flat it
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setTagValueStructureExe(HAPUtilityContext.buildFlatContextFromContextStructure(uiTagExe.getTagValueStructureDefinition()));
		}
		
		//build flat context
//		HAPExecutableValueStructure flatContext = HAPUtilityContext.buildFlatContextFromContextStructure(uiExe.getBody().getValueStructureDefinition());
//		uiExe.getBody().setValueStructureExe(flatContext);

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			flatenContext(childTag);			
		}
	}
}
