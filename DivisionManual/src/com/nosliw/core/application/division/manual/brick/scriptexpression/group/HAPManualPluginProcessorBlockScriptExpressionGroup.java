package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPManualExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.m.HAPManualBrick;
import com.nosliw.core.application.division.manua.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manua.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.xxx.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.scriptexpression.group.HAPBlockScriptExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockScriptExpressionGroup extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockScriptExpressionGroup(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100, HAPManualBlockScriptExpressionGroup.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPDefinitionContainerScriptExpression groupDef = ((HAPManualDefinitionBlockScriptExpressionGroup)blockPair.getLeft()).getValue();
		HAPContainerScriptExpression groupExe = ((HAPBlockScriptExpressionGroup)blockPair.getRight()).getValue();
		HAPManualUtilityScriptExpression.fromDefToExeScriptExpressionContainer(groupDef, groupExe, processContext.getRuntimeEnv().getDataExpressionParser());
	}
	
	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockScriptExpressionGroup groupBlock = (HAPManualBlockScriptExpressionGroup)blockPair.getRight();
		HAPContainerScriptExpression groupExe = ((HAPManualBlockScriptExpressionGroup)blockPair.getRight()).getValue();

		HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();

		for(HAPItemInContainerScriptExpression itemExe : groupExe.getItems()) {
			//resolve constant
			HAPManualUtilityScriptExpression.processScriptExpressionConstant((HAPManualExpressionScript)itemExe.getScriptExpression(), blockPair.getLeft().getConstantDefinitions());
			
			//variable resolve
			HAPUtilityWithVarible.resolveVariable(itemExe.getScriptExpression(), varInfoContainer, null, getManualBrickManager());
			//build variable info in script expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getScriptExpression(), varInfoContainer, getManualBrickManager());
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, groupBlock);
	}
	
	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockScriptExpressionGroup groupBlock = (HAPManualBlockScriptExpressionGroup)blockPair.getRight();
		HAPContainerScriptExpression groupExe = ((HAPManualBlockScriptExpressionGroup)blockPair.getRight()).getValue();

		for(HAPItemInContainerScriptExpression itemExe : groupExe.getItems()) {
			HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();
			Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(itemExe.getScriptExpression(), null, varInfoContainer, getManualBrickManager());
			groupBlock.setVariableInfoContainer(discoverResult.getLeft());
		}
	}

}
