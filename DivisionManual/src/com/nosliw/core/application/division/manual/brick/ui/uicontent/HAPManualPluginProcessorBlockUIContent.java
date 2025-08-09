package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpression;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockUIContent extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockUIContent(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UICONTENT_100, HAPManualBlockComplexUIContent.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();
		
		uiContentExe.setHtml(uiContentDef.getHtml());
		
		uiContentExe.getScriptExpressionInContent().addAll(uiContentDef.getScriptExpressionInContent());
		uiContentExe.getScriptExpressionInNormalTagAttribute().addAll(uiContentDef.getScriptExpressionInNormalTagAttribute());

		//build script expression container
		HAPManualUtilityScriptExpression.fromDefToExeScriptExpressionContainer(uiContentDef.getScriptExpressions(), uiContentExe.getScriptExpressions(), processContext.getRuntimeEnv().getDataExpressionParser());

		//event in normal tag
//		for(HAPElementEvent event : uiContentDef.getNormalTagEvents()) {
//			uiContentExe.addNormalTagEvent(event);
//			event.setTaskInfo(this.locateTask(event.getHandlerName(), uiContentExe, processContext));
//		}

		//event in custom tag
//		for(HAPElementEvent event : uiContentDef.getCustomTagEvent()) {
//			uiContentExe.addCustomTagEvent(event);;
//		}

		
		//customer tag
		
		
	}
	
	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)blockPair.getLeft();
		HAPManualBlockComplexUIContent uiContentExe = (HAPManualBlockComplexUIContent)blockPair.getRight();

		HAPContainerVariableInfo varInfoContainer = uiContentExe.getVariableInfoContainer();

		//resolve variable name, build var info container, build variable info
		HAPManualUtilityScriptExpression.processScriptExpressionContainerVariableResolve(uiContentExe.getScriptExpressions(), varInfoContainer, null, getManualBrickManager());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, processContext.getCurrentBundle().getValueStructureDomain());
		
		
	}
	
	
	
}
