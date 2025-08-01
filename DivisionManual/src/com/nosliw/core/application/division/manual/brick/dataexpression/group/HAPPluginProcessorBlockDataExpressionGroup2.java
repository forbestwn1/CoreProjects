package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.dataexpression.HAPItemInContainerDataExpression;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualExpressionData;
import com.nosliw.core.application.common.dataexpressionimp.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.common.dataexpressionimp.HAPUtilityExpressionProcessor;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.xxx.application1.brick.dataexpression.group.HAPGroupDataExpression;
import com.nosliw.core.xxx.application1.brick.dataexpression.group.HAPItemInGroupDataExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockDataExpressionGroup2 extends HAPManualPluginProcessorBlockComplex{

	public HAPPluginProcessorBlockDataExpressionGroup2(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, HAPManualBlockDataExpressionGroup.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionContainerDataExpression groupDef = ((HAPManualDefinitionBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPGroupDataExpression groupExe = ((HAPBlockDataExpressionGroup)blockPair.getRight()).getValue();
	
		for(HAPManualDefinitionDataExpressionItemInContainer itemDef : groupDef.getItems()) {
			HAPItemInGroupDataExpression itemExe = new HAPItemInGroupDataExpression();
			itemDef.cloneToEntityInfo(itemExe);
			itemExe.setDataExpression(HAPManualUtilityProcessorDataExpression.buildManualDataExpression(itemDef.getExpression()));
			groupExe.addDataExpression(itemExe);
		}
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionContainerDataExpression groupDef = ((HAPManualDefinitionBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPBlockDataExpressionGroup groupBlock = (HAPBlockDataExpressionGroup)blockPair.getRight();
		HAPGroupDataExpression groupExe = ((HAPManualBlockDataExpressionGroup)blockPair.getRight()).getValue();

		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(groupBlock);

		//resolve variable name, build var info container
		for(HAPItemInGroupDataExpression itemExe : groupExe.getItems()) {
			HAPManualUtilityProcessorDataExpression.resolveVariable((HAPManualExpressionData)itemExe.getDataExpressionContainer(), varInfoContainer, null);
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, groupBlock);
		
		//build var info container
		for(HAPItemInContainerDataExpression itemExe : groupExe.getItems()) {
			HAPUtilityExpressionProcessor.buildVariableInfo(groupBlock.getVariablesInfo(), blockPair.getRight());
		}

	}

}
