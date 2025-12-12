package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEventHandlerInfoNormal;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.event.HAPEventDefinition;
import com.nosliw.core.application.common.event.HAPEventReferenceHandler;
import com.nosliw.core.application.common.event.HAPEventReferenceHandlerTask;
import com.nosliw.core.application.common.scriptexpressio.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPUtilityScriptExpression;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafValue;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPStructureImp;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.common.event.HAPManualUtilityEvent;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.application.division.manual.core.process.HAPManualUtilityProcessBrickPath;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPManualPluginProcessorBlockUIContent extends HAPManualPluginProcessorBlockImp{

	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPManualPluginProcessorBlockUIContent(HAPParserDataExpression dataExpressionParser, HAPManagerWithVariablePlugin withVariableMan) {
		super(HAPEnumBrickType.UICONTENT_100, HAPManualBlockComplexUIContent.class);
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_withVariableMan = withVariableMan;
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();
		
		uiContentExe.setHtml(uiContentDef.getHtml());
		
		uiContentExe.getScriptExpressionInContent().addAll(uiContentDef.getScriptExpressionInContent());
		uiContentExe.getScriptExpressionInNormalTagAttribute().addAll(uiContentDef.getScriptExpressionInNormalTagAttribute());
		uiContentExe.getScriptExpressionInCustomerTagAttribute().addAll(uiContentDef.getScriptExpressionInCustomerTagAttribute());

		//build script expression container
		HAPUtilityScriptExpression.fromDefToExeScriptExpressionContainer(uiContentDef.getScriptExpressions(), uiContentExe.getScriptExpressions(), this.m_dataExpressionParser);

		//event in normal tag
		for(HAPUIEventHandlerInfoNormal event : uiContentDef.getNormalTagEvents()) {
			uiContentExe.addNormalTagEvent(event);;
//			event.setTaskInfo(this.locateTask(event.getHandlerName(), uiContentExe, processContext));
		}

		//customer tag
		
	}
	
	@Override
	public void normalizeBrickPath(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockComplexUIContent executableBlock = (HAPManualBlockComplexUIContent)blockPair.getRight();

		for(HAPUIEventHandlerInfoNormal normalTagEventHandlerInfo : executableBlock.getNormalTagEvents()) {
			HAPEventReferenceHandler handler = normalTagEventHandlerInfo.getHandlerInfo();
			String handlerType = handler.getHandlerType();
			if(handlerType.equals(HAPConstantShared.EVENT_HANDLERTYPE_TASK)) {
				HAPEventReferenceHandlerTask handlerTask = (HAPEventReferenceHandlerTask)handler;
				HAPManualUtilityProcessBrickPath.normalizeBrickReferenceInBundle(handlerTask.getTaskBrickId(), pathFromRoot, false, processContext);
			}
		}
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();

		HAPContainerVariableInfo varInfoContainer = uiContentExe.getVariableInfoContainer();

		//resolve variable name, build var info container, build variable info
		HAPUtilityScriptExpression.processScriptExpressionContainerVariableResolve(uiContentExe.getScriptExpressions(), varInfoContainer, null, this.m_withVariableMan, processContext.getRuntimeInfo());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, processContext.getCurrentBundle().getValueStructureDomain());
		
		
	}
	
	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();

		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();
		HAPContainerVariableInfo varInfoContainer = uiContentExe.getVariableInfoContainer();

		for(HAPItemInContainerScriptExpression item : uiContentExe.getScriptExpressions().getItems()) {
			Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(item.getScriptExpression(), null, varInfoContainer, this.m_withVariableMan);
			varInfoContainer = discoverResult.getLeft();
			
			//update value port element according to var info container after discovery
			HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, valueStructureDomain);
		}
	}
	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();

		for(HAPUIEventHandlerInfoNormal eventHandlerInfo : uiContentExe.getNormalTagEvents()) {
			HAPEventReferenceHandler handler = eventHandlerInfo.getHandlerInfo();
			String uiId = eventHandlerInfo.getUIId();
			String eventName = eventHandlerInfo.getEvent();
			String handlerType = handler.getHandlerType();
			if(handlerType.equals(HAPConstantShared.EVENT_HANDLERTYPE_TASK)) {
				HAPEventDefinition eventDef = new HAPEventDefinition();
				eventDef.setName(uiId + "_" + eventName);
				
				HAPStructure structure = new HAPStructureImp();

				//event data
				HAPRootInStructure eventDataRoot = new HAPRootInStructure();
				eventDataRoot.setName(HAPConstantShared.NAME_ROOT_EVENT_DATA);
				eventDataRoot.setDefinition(new HAPElementStructureLeafValue());
				structure.addRoot(eventDataRoot);
				
				eventDef.setDataDefinition(structure);
				HAPManualUtilityEvent.buildValuePortForEventHandlerTask(eventDef, (HAPEventReferenceHandlerTask)handler, processContext.getRootBrickName(), processContext.getCurrentBundle());
			}
		}
	}
	
}
