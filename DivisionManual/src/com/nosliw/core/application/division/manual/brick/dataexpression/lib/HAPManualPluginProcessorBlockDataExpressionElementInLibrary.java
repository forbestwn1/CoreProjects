package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualExpressionData;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualWrapperOperand;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPPluginProcessorEntityWithVariableDataExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockDataExpressionElementInLibrary extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockDataExpressionElementInLibrary(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPElementInLibraryDataExpression exe = ((HAPBlockDataExpressionElementInLibrary)brickInfo.getRight()).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		exe.setExpression(new HAPManualExpressionData(HAPManualUtilityProcessorDataExpression.buildManualOperand(def.getExpression().getOperand())));
		
		//interactive request
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
	}

	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		HAPManualBlockDataExpressionElementInLibrary brick = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight(); 
		HAPElementInLibraryDataExpression exe = brick.getValue();;
		
		HAPGroupValuePorts internalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);
		HAPGroupValuePorts externalValuePortGroup = new HAPGroupValuePorts(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION);

		//request value structure
		Set<HAPRootInStructure> requestRoots = new HashSet<HAPRootInStructure>();
		Map<String, HAPData> defaultValue = new LinkedHashMap<String, HAPData>(); 
		List<HAPRequestParmInInteractive> requestParms = exe.getInteractive().getRequestParms();
		for(HAPRequestParmInInteractive parm : requestParms) {
			HAPRootInStructure root = new HAPRootInStructure(new HAPElementStructureLeafData(parm.getCriteria()), parm);
			requestRoots.add(root);
			defaultValue.put(parm.getName(), parm.getDefaultValue());
		}
		String requestVSId = valueStructureDomain.newValueStructure(requestRoots, defaultValue, null, null);

		//result value structure
		Set<HAPRootInStructure> resultRoots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure resultRoot = new HAPRootInStructure(new HAPElementStructureLeafData(exe.getInteractive().getResult().getDataCriteria()), null);
		resultRoot.setName(HAPConstantShared.NAME_ROOT_RESULT);
		resultRoots.add(resultRoot);
		String resultVSId = valueStructureDomain.newValueStructure(resultRoots, null, null, null);

		
		//request value port -- internal
		HAPValuePort internalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_BOTH);
		internalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		internalReqeustValuePort.addValueStructureId(requestVSId);
		internalValuePortGroup.addValuePort(internalReqeustValuePort, true);
		
		//request value port -- external
		HAPValuePort externalReqeustValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, HAPConstantShared.IO_DIRECTION_IN);
		externalReqeustValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		externalReqeustValuePort.addValueStructureId(requestVSId);
		externalValuePortGroup.addValuePort(externalReqeustValuePort, true);
		
		HAPValuePort internalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_IN);
		internalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		internalResultValuePort.addValueStructureId(resultVSId);
		internalValuePortGroup.addValuePort(internalResultValuePort, true);
		
		HAPValuePort externalResultValuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, HAPConstantShared.IO_DIRECTION_BOTH);
		externalResultValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		externalResultValuePort.addValueStructureId(resultVSId);
		externalValuePortGroup.addValuePort(externalResultValuePort, true);
		
		brick.getOtherInternalValuePortContainer().addValuePortGroup(internalValuePortGroup);
		brick.getOtherExternalValuePortContainer().addValuePortGroup(externalValuePortGroup);
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionElementInLibrary blockExe = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight();
		HAPElementInLibraryDataExpression exe = blockExe.getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();
		
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)exe.getExpression();

		HAPContainerVariableInfo varInfoContainer = blockExe.getVariableInfoContainer();

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, null, getManualBrickManager());
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpression, varInfoContainer, getManualBrickManager());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, valueStructureDomain);
	}

	@Override
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentBundle().getValueStructureDomain();

		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfo = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockDataExpressionElementInLibrary blockExe = (HAPManualBlockDataExpressionElementInLibrary)brickInfo.getRight();
		HAPElementInLibraryDataExpression exe = blockExe.getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)brickInfo.getLeft()).getValue();

		HAPContainerVariableInfo varInfoContainer = blockExe.getVariableInfoContainer();
		
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)exe.getExpression();
		HAPManualWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPPluginProcessorEntityWithVariableDataExpression.RESULT, exe.getInteractive().getResult().getDataCriteria());
		Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainer, getManualBrickManager());
		varInfoContainer = discoverResult.getLeft();
		
		//update value port element according to var info container after discovery
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, valueStructureDomain);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(exe.getInteractive().getResult().getDataCriteria()==null) {
			exe.getInteractive().getResult().setDataCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(discoverResult.getRight().get(HAPPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
	}

/*	
	
	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {
		HAPElementInLibraryDataExpression exe = ((HAPBlockDataExpressionElementInLibrary)blockExe).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)blockDef).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		exe.setExpression(new HAPManualExpressionData(HAPManualUtilityProcessorDataExpression.buildManualOperand(def.getExpression().getOperand())));
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)exe.getExpression();
		
		//interactive request
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		HAPManualWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(blockExe, processContext.getCurrentBundle().getValueStructureDomain());

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, null, getManualBrickManager());
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpression, varInfoContainer, getManualBrickManager());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, blockExe);

		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPPluginProcessorEntityWithVariableDataExpression.RESULT, exe.getInteractive().getResult().getDataCriteria());
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
			exe.setResultMatchers(discoverResult.getRight().get(HAPPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
	}
	*/	
}
