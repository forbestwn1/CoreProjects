package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.brick.dataexpression.group.HAPGroupDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPElementInContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockComplexImp;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPUtilityExpressionProcessor;

public class HAPPluginProcessorBlockDataExpressionGroup extends HAPPluginProcessorBlockComplexImp{

	public HAPPluginProcessorBlockDataExpressionGroup() {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualBrick, HAPBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDataExpressionGroup groupDef = ((HAPManualBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPGroupDataExpression groupExe = ((HAPBlockDataExpressionGroup)blockPair.getRight()).getValue();
	
		for(HAPManualDataExpressionItemInGroup itemDef : groupDef.getItems()) {
			HAPElementInContainerDataExpression itemExe = new HAPElementInContainerDataExpression();
			itemDef.cloneToEntityInfo(itemExe);
			
			//build expression in executable
			HAPOperand operand = processContext.getRuntimeEnv().getDataExpressionParser().parseExpression(itemDef.getExpression());
			itemExe.setExpression(new HAPDataExpression(new HAPWrapperOperand(operand)));
			groupExe.addItem(itemExe);
		}
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualBrick, HAPBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDataExpressionGroup groupDef = ((HAPManualBlockDataExpressionGroup)blockPair.getLeft()).getValue();
		HAPBlockDataExpressionGroup groupBlock = (HAPBlockDataExpressionGroup)blockPair.getRight();
		HAPContainerDataExpression groupExe = ((HAPBlockDataExpressionGroup)blockPair.getRight()).getValue();
	
		//resolve variable name, build var info container
		for(HAPElementInContainerDataExpression itemExe : groupExe.getItems()) {
			HAPUtilityExpressionProcessor.resolveVariableName(itemExe.getExpression(), blockPair.getRight(), groupBlock.getVariablesInfo(), null);
		}
		
		//build var info container
		for(HAPElementInContainerDataExpression itemExe : groupExe.getItems()) {
			HAPUtilityExpressionProcessor.buildVariableInfo(groupBlock.getVariablesInfo(), blockPair.getRight());
		}

	}

}
