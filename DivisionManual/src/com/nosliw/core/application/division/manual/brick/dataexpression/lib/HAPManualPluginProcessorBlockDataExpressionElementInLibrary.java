package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
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

public class HAPManualPluginProcessorBlockDataExpressionElementInLibrary extends HAPManualPluginProcessorBlockImp{

	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPManualPluginProcessorBlockDataExpressionElementInLibrary(HAPManagerWithVariablePlugin withVariableMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class);
		this.m_withVariableMan = withVariableMan;
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPDataExpressionStandAlone exe = ((HAPBlockDataExpressionElementInLibrary)brickInfo.getRight()).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		
		((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).cloneToEntityInfo(((HAPBlockDataExpressionElementInLibrary)brickInfo.getRight()));
		
		//build expression in executable
		exe.setExpression(new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildBasicOperand(def.getExpression().getOperand())));
		
		//interactive request
		exe.setExpressionInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
	}

	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		HAPManualBlockDataExpressionElementInLibrary brick = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight(); 
		HAPDataExpressionStandAlone exe = brick.getValue();;
		HAPManualUtilityTask.buildValuePortGroupForInteractiveExpression(brick, exe.getExpressionInterface(), valueStructureDomain);
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionElementInLibrary blockExe = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight();
		HAPDataExpressionStandAlone exe = blockExe.getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		
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
		HAPManualBlockDataExpressionElementInLibrary blockExe = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight();
		HAPDataExpressionStandAlone exe = blockExe.getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();

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

/*	
	
	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {
		HAPDataExpressionStandAlone exe = ((HAPBlockDataExpressionElementInLibrary)blockExe).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)blockDef).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		exe.setExpression(new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildManualOperand(def.getExpression().getOperand())));
		HAPBasicExpressionData dataExpression = (HAPBasicExpressionData)exe.getExpression();
		
		//interactive request
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		HAPBasicWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(blockExe, processContext.getCurrentBundle().getValueStructureDomain());

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, null, getManualBrickManager());
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpression, varInfoContainer, getManualBrickManager());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, blockExe);

		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT, exe.getInteractive().getResult().getDataCriteria());
		Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainer, getManualBrickManager());
		varInfoContainer = discoverResult.getLeft();
		
		//update value port element according to var info container after discovery
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, blockExe);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(exe.getInteractive().getResult().getDataCriteria()==null) {
			exe.getInteractive().getResult().setDataCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(discoverResult.getRight().get(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
	}
	*/	
}
