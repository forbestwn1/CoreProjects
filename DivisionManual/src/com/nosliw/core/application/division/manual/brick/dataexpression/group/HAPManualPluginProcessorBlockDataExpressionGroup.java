package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualExpressionData;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockDataExpressionGroup extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockDataExpressionGroup(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualBlockDataExpressionGroup.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionContainerDataExpression groupDef = ((HAPManualDefinitionBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPContainerDataExpression groupExe = ((HAPBlockDataExpressionGroup)blockPair.getRight()).getValue();
	
		for(HAPManualDefinitionDataExpressionItemInContainer itemDef : groupDef.getItems()) {
			HAPItemInContainerDataExpression itemExe = new HAPItemInContainerDataExpression();
			itemDef.cloneToEntityInfo(itemExe);
			itemExe.setDataExpression(HAPManualUtilityProcessorDataExpression.buildManualDataExpression(itemDef.getExpression()));
			groupExe.addItem(itemExe);
		}
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)blockPair.getRight();
		HAPContainerDataExpression groupExe = ((HAPManualBlockDataExpressionGroup)blockPair.getRight()).getValue();

		HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();

		for(HAPItemInContainerDataExpression itemExe : groupExe.getItems()) {
			//resolve constants
			HAPManualUtilityProcessorDataExpression.processConstant((HAPManualExpressionData)itemExe.getDataExpression(), blockPair.getLeft().getConstantDefinitions());
			
			//resolve variable name
			HAPUtilityWithVarible.resolveVariable(itemExe.getDataExpression(), varInfoContainer, null, getManualBrickManager());
			//build variable info in data expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getDataExpression(), varInfoContainer, getManualBrickManager());
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, processContext.getCurrentBundle().getValueStructureDomain());
	}

	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionGroup groupBlock = (HAPManualBlockDataExpressionGroup)blockPair.getRight();
		HAPContainerDataExpression groupExe = ((HAPManualBlockDataExpressionGroup)blockPair.getRight()).getValue();

		for(HAPItemInContainerDataExpression itemExe : groupExe.getItems()) {
			HAPContainerVariableInfo varInfoContainer = groupBlock.getVariableInfoContainer();
			Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(itemExe.getDataExpression(), null, varInfoContainer, getManualBrickManager());
			groupBlock.setVariableInfoContainer(discoverResult.getLeft());
		}
	}
	
}
