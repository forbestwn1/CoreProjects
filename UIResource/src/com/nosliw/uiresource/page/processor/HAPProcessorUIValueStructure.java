package com.nosliw.uiresource.page.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafValue;
import com.nosliw.core.application.common.structure.HAPProcessorEscalate;
import com.nosliw.core.application.common.structure.HAPProcessorStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPTreeNodeValueStructure;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIValueStructure {

	public static void enhanceValueStructure(HAPDefinitionUIUnit uiUnitDef) {
		HAPValueStructureDefinitionGroup valueStructure = (HAPValueStructureDefinitionGroup)uiUnitDef.getValueStructureBlock();

		//ui error value structure element
		HAPRootStructure uiValidationErrorRoot = new HAPRootStructure(new HAPElementStructureLeafValue());
		uiValidationErrorRoot.setDefaultValue(new JSONObject());
		uiValidationErrorRoot.setName(HAPConstantShared.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR);
		valueStructure.addProtectedElement(uiValidationErrorRoot);

		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			enhanceValueStructure(uiTag);
		}
	}
	
	public static void buildExecutable(HAPExecutableUIUnit1 uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUITag uiTagExe = (HAPExecutableUITag)uiExe;
			uiTagExe.setTagValueStructureExe(HAPUtilityValueStructure.buildExecuatableValueStructure(uiTagExe.getTagValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock()));
		}
		
//		uiExe.getBody().setValueStructureExe(HAPUtilityValueStructure.buildExecuatableValueStructure(uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructure()));
		
		//child tag
		for(HAPExecutableUITag childTag : uiExe.getBody().getUITags()) {
			buildExecutable(childTag);
		}
	}
	
	public static void process(HAPExecutableUIUnitPage uiPageExe, HAPValueStructureDefinitionGroup parentValueStructure, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableUIUnit body = uiPageExe.getBody();
		HAPConfigureProcessorValueStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(uiPageExe.getResourceType()); 

		HAPConfigureProcessorValueStructure privateConfigure = contextProcessorConfig.cloneConfigure();
		privateConfigure.parentCategary = HAPValueStructureDefinitionGroup.getAllCategaries();

		//build value structure tree
		buildValueStructureTree(uiPageExe, null, uiTagMan);
		
		//process static in value structure
		processStatic(uiPageExe, parentValueStructure, contextProcessorConfig, uiTagMan, runtimeEnv);		

		//escalate
		for(HAPExecutableUITag childTag : uiPageExe.getBody().getUITags()) {
			processEscalate(childTag, uiTagMan, contextProcessorConfig.inheritanceExcludedInfo);
		}
		
		//process relative element in value structure
		processRelativeElement(uiPageExe, parentValueStructure, privateConfigure, uiTagMan, runtimeEnv);
		
		//mark value structure as processed
		markProcessed(uiPageExe);
		
		
//		flatenContext(uiExe);
	}
	
	//build value structure tree 
	private static void buildValueStructureTree(HAPExecutableUIUnit1 unitExe, HAPTreeNodeValueStructure parentNode, HAPManagerUITag uiTagMan){
		
		if(unitExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUITag uiTagExe = (HAPExecutableUITag)unitExe;
			HAPUITagDefinition uiTagDef = uiTagMan.getUITagDefinition(new HAPUITagId(uiTagExe.getUIUnitTagDefinition().getTagName()));
			uiTagExe.setUITagDefinition(uiTagDef);
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			nodeInTag.setValueStructureWrapper(uiTagDef.getValueStructureDefinitionWrapper());
			nodeInTag.setParent(parentNode);
			parentNode = nodeInTag;
		}
		
		HAPTreeNodeValueStructure nodeInBody = unitExe.getBody().getValueStructureDefinitionNode();
//		nodeInBody.setValueStructureWrapper(unitExe.getUIUnitDefinition().getValueStructureWrapper());
		nodeInBody.setParent(parentNode);
		
		//child tag
		for(HAPExecutableUITag childTag : unitExe.getBody().getUITags()) {
			buildValueStructureTree(childTag, nodeInBody, uiTagMan);
		}
	}
	
	//process static in value structure
	private static void processStatic(HAPExecutableUIUnit1 unitExe, HAPValueStructure parentStructure, HAPConfigureProcessorValueStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(unitExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUITag uiTagExe = (HAPExecutableUITag)unitExe;
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			nodeInTag.getValueStructureWrapper().setValueStructure(HAPUtilityProcess.buildUITagValueStructure(uiTagExe.getUITagDefinition(), (HAPValueStructureDefinitionGroup)parentStructure, uiTagExe.getAttributes(), contextProcessorConfig, runtimeEnv));
			parentStructure = nodeInTag.getValueStructureWrapper().getValueStructureBlock();
		}

		HAPTreeNodeValueStructure nodeInBody = unitExe.getBody().getValueStructureDefinitionNode();
		nodeInBody.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processStatic(nodeInBody.getValueStructureWrapper().getValueStructureBlock(), HAPContainerStructure.createDefault(parentStructure), unitExe.getUIUnitDefinition().getAttachmentContainer(), null, contextProcessorConfig, runtimeEnv));
		
		//child tag
		for(HAPExecutableUITag childTag : unitExe.getBody().getUITags()) {
			//merge with context defined in tag unit
			processStatic(childTag, nodeInBody.getValueStructureWrapper().getValueStructureBlock(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
	}

	//process escalate element to parent
	private static void processEscalate(HAPExecutableUITag exeUITag, HAPManagerUITag uiTagMan, Set<String> inheritanceExcludedInfo) {
		//context
		if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeUITag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
			Set<String> categarys = new HashSet<String>();
			categarys.add(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			Map<String, String> contextMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeUITag.getAttributes().get(HAPConstantShared.UITAG_PARM_CONTEXT));
			exeUITag.setContextMapping(contextMapping);
			HAPProcessorEscalate.process(exeUITag.getBody().getValueStructureDefinitionNode(), categarys, contextMapping, inheritanceExcludedInfo);
		}
		
		for(HAPExecutableUITag childTag : exeUITag.getBody().getUITags()) {
			processEscalate(childTag, uiTagMan, inheritanceExcludedInfo);
		}
	}
	
	private static void processRelativeElement(HAPExecutableUIUnit1 uiExe, HAPValueStructure parentStructure, HAPConfigureProcessorValueStructure contextProcessorConfig, HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv){
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUITag uiTagExe = (HAPExecutableUITag)uiExe;
			HAPTreeNodeValueStructure nodeInTag = uiTagExe.getTagValueStructureDefinitionNode();
			if(parentStructure==null)  parentStructure = HAPUtilityValueStructure.getValueStructureFromWrapper(nodeInTag.getParent().getValueStructureWrapper());
			//process relative element in tag context
			nodeInTag.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(nodeInTag.getValueStructureWrapper().getValueStructureBlock(), HAPContainerStructure.createDefault(parentStructure), null, contextProcessorConfig, runtimeEnv));
			parentStructure = nodeInTag.getValueStructureWrapper().getValueStructureBlock();
			
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
		if(parentStructure==null && nodeInBody.getParent()!=null)  parentStructure = nodeInBody.getParent().getValueStructureWrapper().getValueStructureBlock();
		
		nodeInBody.getValueStructureWrapper().setValueStructure((HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(nodeInBody.getValueStructureWrapper().getValueStructureBlock(), HAPContainerStructure.createDefault(parentStructure), null, contextProcessorConfig, runtimeEnv));

		//child tag
		for(HAPExecutableUITag childTag : uiExe.getBody().getUITags()) {
			processRelativeElement(childTag, nodeInBody.getValueStructureWrapper().getValueStructureBlock(), contextProcessorConfig, uiTagMan, runtimeEnv);			
		}
		
	}	
	
	private static void markProcessed(HAPExecutableUIUnit1 uiExe) {
		if(uiExe.getType().equals(HAPConstantShared.UIRESOURCE_TYPE_TAG)) {
			HAPExecutableUITag uiTagExe = (HAPExecutableUITag)uiExe;
			uiTagExe.getTagValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock().processed();
		}

		uiExe.getBody().getValueStructureDefinitionNode().getValueStructureWrapper().getValueStructureBlock().processed();
		
		//child tag
		for(HAPExecutableUITag childTag : uiExe.getBody().getUITags()) {
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
