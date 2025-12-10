package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIHandlerEventTagCustom;
import com.nosliw.core.application.common.event.HAPEventDefinition;
import com.nosliw.core.application.common.event.HAPEventInfoHandler;
import com.nosliw.core.application.common.event.HAPEventInfoHandlerTask;
import com.nosliw.core.application.division.manual.common.event.HAPManualUtilityEvent;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.application.division.manual.core.process.HAPManualUtilityProcessBrickPath;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttribute;

public class HAPManualPluginProcessorBlockUICustomerTag extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockUICustomerTag() {
		super(HAPEnumBrickType.UICUSTOMERTAG_100, HAPManualBlockComplexUICustomerTag.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUICustomerTag uiCustomerTagDef = (HAPManualDefinitionBlockComplexUICustomerTag)blockPair.getLeft();
		HAPManualBlockComplexUICustomerTag uiCustomerTagExe = (HAPManualBlockComplexUICustomerTag)blockPair.getRight();

		uiCustomerTagExe.setUITagDefinition(uiCustomerTagDef.getUITagDefinition());
		
		Map<String, String> attrValues = uiCustomerTagDef.getTagAttributes();
		for(String attrName : attrValues.keySet()) {
			uiCustomerTagExe.addTagAttribute(attrName, attrValues.get(attrName));
		}

		Map<String, HAPUITagDefinitionAttribute> attrDefs = uiCustomerTagDef.getTagAttributeDefinitions();
		for(String attrName : attrDefs.keySet()) {
			uiCustomerTagExe.addAttributeDefinition(attrDefs.get(attrName));
		}
		
		uiCustomerTagExe.setUIId(uiCustomerTagDef.getUIId());
		if(uiCustomerTagDef.getBase()!=null) {
			uiCustomerTagExe.setBase(uiCustomerTagDef.getBase());
		}
		
		Map<String, String> metaData = uiCustomerTagDef.getMetaData();
		for(String key : metaData.keySet()) {
			uiCustomerTagExe.addMetaData(key, metaData.get(key));
		}
		uiCustomerTagExe.addMetaData(HAPConstantShared.UITAG_METADATA_UITAGNAME, uiCustomerTagDef.getUITagDefinition().getName());
		uiCustomerTagExe.addMetaData(HAPConstantShared.UITAG_METADATA_UITAGVERSION, uiCustomerTagDef.getUITagDefinition().getVersion());
		
		//event in custom tag
		for(HAPUIHandlerEventTagCustom event : uiCustomerTagDef.getEvents()) {
			uiCustomerTagExe.addEvent(event);
		}


		
		if(uiCustomerTagDef.getScriptResourceId()!=null) {
			uiCustomerTagExe.setScriptResourceId(uiCustomerTagDef.getScriptResourceId());
		}
	}

	@Override
	public void normalizeBrickPath(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockComplexUICustomerTag executableBlock = (HAPManualBlockComplexUICustomerTag)blockPair.getRight();

		for(HAPUIHandlerEventTagCustom eventHandler : executableBlock.getEvents().values()) {
			HAPEventInfoHandler handler = eventHandler.getHandlerInfo();
			String handlerType = handler.getHandlerType();
			if(handlerType.equals(HAPConstantShared.EVENT_HANDLERTYPE_TASK)) {
				HAPEventInfoHandlerTask handlerTask = (HAPEventInfoHandlerTask)handler;
				HAPManualUtilityProcessBrickPath.normalizeBrickReferenceInBundle(handlerTask.getTaskBrickId(), pathFromRoot, false, processContext);
			}
		}
	}
	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUICustomerTag blockDef = (HAPManualDefinitionBlockComplexUICustomerTag)blockPair.getLeft();
		HAPManualBlockComplexUICustomerTag executableBlock = (HAPManualBlockComplexUICustomerTag)blockPair.getRight();

		for(HAPUIHandlerEventTagCustom eventHandler : executableBlock.getEvents().values()) {
			HAPEventInfoHandler handler = eventHandler.getHandlerInfo();
			String handlerType = handler.getHandlerType();
			if(handlerType.equals(HAPConstantShared.EVENT_HANDLERTYPE_TASK)) {
				HAPEventDefinition eventDef = blockDef.getUITagDefinition().getEventDefinition(eventHandler.getEvent());
				HAPManualUtilityEvent.buildValuePortForEventHandlerTask(eventDef, (HAPEventInfoHandlerTask)handler, processContext.getRootBrickName(), processContext.getCurrentBundle());
			}
		}
	}

}
