package com.nosliw.core.application.division.manual.brick.task.wrapper.dataexpression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.expression.dataexpression.standalone.HAPBlockDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicExpressionData;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicPluginProcessorEntityWithVariableDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicUtilityProcessorDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicWrapperOperand;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPManualPluginProcessorBlockTaskWrapperDataExpression extends HAPManualPluginProcessorBlockImp{

	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPManualPluginProcessorBlockTaskWrapperDataExpression(HAPManagerWithVariablePlugin withVariableMan) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockDataTaskWrapperDataExpression.class);
		this.m_withVariableMan = withVariableMan;
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPDataExpressionStandAlone exe = ((HAPBlockDataExpressionStandAlone)brickInfo.getRight()).getValue();;
		HAPDefinitionDataExpressionStandAlone def = ((HAPManualDefinitionBlockTaskWrapperDataExpression)brickInfo.getLeft()).getDataExpression();
		
		//build expression in executable
		exe.setExpression(new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildBasicOperand(def.getExpression().getOperand())));
		
		//interactive request
		exe.setExpressionInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
	}

	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataTaskWrapperDataExpression brick = (HAPManualBlockDataTaskWrapperDataExpression)brickInfo.getRight(); 
		HAPManualUtilityTask.buildValuePortGroupForInteractiveExpression(brick, brick.getDataExpression().getExpressionInterface(), valueStructureDomain);
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataTaskWrapperDataExpression blockExe = (HAPManualBlockDataTaskWrapperDataExpression)brickInfo.getRight();
		HAPDataExpressionStandAlone exe = blockExe.getDataExpression();
		HAPDefinitionDataExpressionStandAlone def = ((HAPManualDefinitionBlockTaskWrapperDataExpression)brickInfo.getLeft()).getDataExpression();
		
		HAPBasicExpressionData dataExpression = (HAPBasicExpressionData)exe.getExpression();

		HAPContainerVariableInfo varInfoContainer = blockExe.getVariableInfoContainer();

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, null, this.m_withVariableMan, processContext.getRuntimeInfo());
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpression, varInfoContainer, this.m_withVariableMan);
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, valueStructureDomain);
	}

	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataTaskWrapperDataExpression blockExe = (HAPManualBlockDataTaskWrapperDataExpression)brickInfo.getRight();
		HAPDataExpressionStandAlone exe = blockExe.getDataExpression();
		HAPDefinitionDataExpressionStandAlone def = ((HAPManualDefinitionBlockTaskWrapperDataExpression)brickInfo.getLeft()).getDataExpression();

		HAPContainerVariableInfo varInfoContainer = blockExe.getVariableInfoContainer();
		
		HAPBasicExpressionData dataExpression = (HAPBasicExpressionData)exe.getExpression();
		HAPBasicWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT, exe.getExpressionInterface().getResult().getDataDefinition().getCriteria());
		Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainer, this.m_withVariableMan);
		varInfoContainer = discoverResult.getLeft();
		
		//update value port element according to var info container after discovery
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, valueStructureDomain);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(exe.getExpressionInterface().getResult().getDataDefinition().getCriteria()==null) {
			exe.getExpressionInterface().getResult().getDataDefinition().setCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(discoverResult.getRight().get(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
	}
}
