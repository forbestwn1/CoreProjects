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
import com.nosliw.data.core.structure.HAPProcessorEscalate;
import com.nosliw.data.core.structure.HAPProcessorStructure;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPTreeNodeValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIValueStructure {

	//expand context reference by using context definition in attachment
	public static void expandContextReference(HAPDefinitionUIUnit uiUnitDef, HAPManagerResourceDefinition resourceDefMan) {
		HAPUtilityComponent.resolveContextReference(uiUnitDef.getValueStructureWrapper().getValueStructure(), uiUnitDef.getContextReferences(), uiUnitDef.getAttachmentContainer(), resourceDefMan);
		
		//process child tags
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			expandContextReference(uiTag, resourceDefMan);
		}
	}
	
	public static void enhanceContextByService(HAPDefinitionUIUnit uiUnitDef, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : uiUnitDef.getAllServices()) {
			HAPDefinitionServiceUse service = uiUnitDef.getService(serviceName);
			HAPProcessorServiceUse.enhanceContextByService(service, uiUnitDef.getValueStructureWrapper().getValueStructure(), runtimeEnv);
		}
		
		//child tag
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			enhanceContextByService(uiTag, runtimeEnv);
		}
	}
	
	public static void buildExecutable(HAPExecutableUIUnit uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.setTagValueStructureExe(HAPUtilityValueStructure.buildExecuatableValueStructure(uiTagExe.getTagValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()));
		}
		
		uiExe.getBody().setValueStructureExe(HAPUtilityValueStructure.buildExecuatableValueStructure(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()));
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			buildExecutable(childTag);
		}
	}
	
	public static void process(HAPExecutableUIUnitPage uiPageExe, HAPValueStructureDefinitionGroup parentValueStructure, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIBody body = uiPageExe.getBody();
		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiPageExe.getType()); 

		HAPConfigureProcessorStructure privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPValueStructureDefinitionGroup.getAllCategaries();

		//build value structure tree
		buildValueStructureTree(uiPageExe, null, uiTagMan);
		
		//process static in value structure
		processStatic(uiPageExe, parentValueStructure, contextProcessorConfig, uiTagMan, runtimeEnv);		

		//escalate
		for(HAPExecutableUIUnitTag childTag : uiPageExe.getBody().getUITags()) {
			processEscalate(childTag, uiTagMan, contextProcessorConfig.inheritanceExcludedInfo);
		}
		
		//process relative element in value structure
		processRelativeElement(uiPageExe, parentValueStructure, privateConfigure, uiTagMan, runtimeEnv);
		
		//mark value structure as processed
		markProcessed(uiPageExe);
		
		
//		flatenContext(uiExe);
	}
	
	//build value structure tree 
	private static void buildValueStructureTree(HAPExecutableUIUnit unitExe, HAPTreeNodeValueStructure parentNode, HAPManagerUITag uiTagMan){
		
		if(unitExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)unitExe;
			HAPUITagDefinition uiTagDef = uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName()));
			uiTagExe.setUITagDefinition(uiTagDef);
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			nodeInTag.setValueStructureWrapper(uiTagDef.getValueStructureDefinitionWrapper());
			nodeInTag.setParent(parentNode);
			parentNode = nodeInTag;
		}
		
		HAPTreeNodeValueStructure nodeInBody = unitExe.getBody().getValueStructureDefinitionNode();
		nodeInBody.setValueStructureWrapper(unitExe.getUIUnitDefinition().getValueStructureWrapper());
		nodeInBody.setParent(parentNode);
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : unitExe.getBody().getUITags()) {
			buildValueStructureTree(childTag, nodeInBody, uiTagMan);
		}
	}
	
	//process static in value structure
	private static void processStatic(HAPExecutableUIUnit unitExe, HAPValueStructure parentStructure, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(unitExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)unitExe;
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			nodeInTag.getValueStructureWrapper().setValueStructure(HAPUtilityProcess.buildUITagValueStructure(uiTagExe.getUITagDefinition(), (HAPValueStructureDefinitionGroup)parentStructure, uiTagExe.getAttributes(), contextProcessorConfig, runtimeEnv));
			parentStructure = nodeInTag.getValueStructureWrapper().getValueStructure();
		}

		HAPTreeNodeValueStructure nodeInBody = unitExe.getBody().getValueStructureDefinitionNode();
		nodeInBody.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processStatic(nodeInBody.getValueStructureWrapper().getValueStructure(), HAPContainerStructure.createDefault(parentStructure), unitExe.getUIUnitDefinition().getAttachmentContainer(), null, contextProcessorConfig, runtimeEnv));
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : unitExe.getBody().getUITags()) {
			//merge with context defined in tag unit
			processStatic(childTag, nodeInBody.getValueStructureWrapper().getValueStructure(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}

	//process escalate element to parent
	private static void processEscalate(HAPExecutableUIUnitTag exeUITag, HAPManagerUITag uiTagMan, Set<String> inheritanceExcludedInfo) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstantShared.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPProcessorEscalate.process(exeUITag.getBody().getValueStructureDefinitionNode(), categarys, contextMapping, inheritanceExcludedInfo);
		}
		
		for(HAPExecutableUIUnitTag childTag : exeUITag.getBody().getUITags()) {
			processEscalate(childTag, uiTagMan, inheritanceExcludedInfo);
		}
	}
	
	private static void processRelativeElement(HAPExecutableUIUnit uiExe, HAPValueStructure parentStructure, HAPConfigureProcessorStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			if(parentStructure==null)  parentStructure = HAPUtilityValueStructure.getValueStructureFromWrapper(nodeInTag.getParent().getValueStructureWrapper());
			//process relative element in tag context
			nodeInTag.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(nodeInTag.getValueStructureWrapper().getValueStructure(), HAPContainerStructure.createDefault(parentStructure), null, contextProcessorConfig, runtimeEnv));
			parentStructure = nodeInTag.getValueStructureWrapper().getValueStructure();
			
//			//process relative element in event definition in tag
//			for(HAPDefinitionUIEvent eventDef : uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName())).getEventDefinition()) {
//				HAPDefinitionUIEvent processedEventDef = new HAPDefinitionUIEvent();
//				eventDef.cloneToBase(processedEventDef);
//				processedEventDef.setDataDefinition((HAPValueStructureDefinitionFlat)HAPProcessorElementRelative.process(eventDef.getDataDefinition(), HAPContainerStructure.createDefault(uiTagExe.getTagValueStructureDefinition()), null, contextProcessorConfig, runtimeEnv));
//				uiTagExe.addTagEvent(eventDef.getName(), processedEventDef);
//			}
		}

		//merge with context defined in resource
		HAPTreeNodeValueStructure nodeInBody = uiExe.getBody().getValueStructureDefinitionNode();
		if(parentStructure==null && nodeInBody.getParent()!=null)  parentStructure = nodeInBody.getParent().getValueStructureWrapper().getValueStructure();
		
		nodeInBody.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(nodeInBody.getValueStructureWrapper().getValueStructure(), HAPContainerStructure.createDefault(parentStructure), null, contextProcessorConfig, runtimeEnv));

		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			processRelativeElement(childTag, nodeInBody.getValueStructureWrapper().getValueStructure(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
		
	}	
	
	private static void markProcessed(HAPExecutableUIUnit uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
			uiTagExe.getTagValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure().processed();
		}

		uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure().processed();
		
		//child tag
		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
			markProcessed(childTag);			
		}
	}
	

	
//	private static void flatenContext(HAPExecutableUIUnit uiExe){
//		
//		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
//			//flat it
//			HAPExecutableUIUnitTag uiTagExe = (HAPExecutableUIUnitTag)uiExe;
//			uiTagExe.setTagValueStructureExe(HAPUtilityContext.buildFlatContextFromContextStructure(uiTagExe.getTagValueStructureDefinition()));
//		}
//		
//		//build flat context
////		HAPExecutableValueStructure flatContext = HAPUtilityContext.buildFlatContextFromContextStructure(uiExe.getBody().getValueStructureDefinition());
////		uiExe.getBody().setValueStructureExe(flatContext);
//
//		//child tag
//		for(HAPExecutableUIUnitTag childTag : uiExe.getBody().getUITags()) {
//			flatenContext(childTag);			
//		}
//	}
}
