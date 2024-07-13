package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.brick.dataexpression.group.HAPGroupDataExpression;
import com.nosliw.core.application.brick.dataexpression.group.HAPItemInGroupDataExpression;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockDataExpressionGroup extends HAPPluginProcessorBlockComplex{

	public HAPPluginProcessorBlockDataExpressionGroup(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualBlockDataExpressionGroup.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionDataExpressionGroup groupDef = ((HAPManualDefinitionBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPGroupDataExpression groupExe = ((HAPBlockDataExpressionGroup)blockPair.getRight()).getValue();
	
		for(HAPManualDefinitionDataExpressionItemInGroup itemDef : groupDef.getItems()) {
			HAPItemInGroupDataExpression itemExe = new HAPItemInGroupDataExpression();
			itemDef.cloneToEntityInfo(itemExe);
			itemExe.setDataExpression(HAPManualUtilityProcessorDataExpression.buildManualDataExpression(itemDef.getExpression()));
			groupExe.addItem(itemExe);
		}
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)blockPair.getRight();
		HAPGroupDataExpression groupExe = ((HAPManualBlockDataExpressionGroup)blockPair.getRight()).getValue();

		HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();

		//resolve variable name, build var info container
		for(HAPItemInGroupDataExpression itemExe : groupExe.getItems()) {
			HAPUtilityWithVarible.resolveVariable(itemExe.getDataExpression(), varInfoContainer, null, getManualBrickManager());
			//build variable info in data expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getDataExpression(), varInfoContainer, getManualBrickManager());
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, groupBlock);
	}

	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)blockPair.getRight();
		HAPGroupDataExpression groupExe = ((HAPManualBlockDataExpressionGroup)blockPair.getRight()).getValue();

		for(HAPItemInGroupDataExpression itemExe : groupExe.getItems()) {
			HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();
			Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(itemExe.getDataExpression(), null, varInfoContainer, getManualBrickManager());
			groupBlock.setVariableInfoContainer(discoverResult.getLeft());
		}
	}
	
}
