package com.nosliw.core.application.division.manual.common.task;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafValue;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPInfoValuePortContainer;
import com.nosliw.core.application.valueport.HAPUtilityBrickValuePort;
import com.nosliw.core.application.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.valueport.HAPValuePort;
import com.nosliw.core.application.valueport.HAPWithExternalValuePort;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.criteria.HAPParserCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;

public class HAPManualUtilityTask {

	public static String getBrickTaskType(HAPIdBrickType brickTypeId, HAPManualManagerBrick brickManManual) {
		return brickManManual.getBrickTypeInfo(brickTypeId).getTaskType();
	}
	
	public static HAPPath figureoutTaskPath(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide) {
		HAPPath out = idPath;
		HAPBrick brick = HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(brick!=null&&brick.getBrickType().equals(HAPEnumBrickType.TASKWRAPPER_100)) {
			out = idPath.appendSegment(HAPBlockTaskWrapper.TASK);
		}
		return out;
	}
	
	public static String getExternalValuePortGroupNameOfInteractiveTask(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		idPath = figureoutTaskPath(bundle, idPath, rootNameIfNotProvide);
		HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrickValuePort.getDescdentValuePortContainerInfo(bundle, rootNameIfNotProvide, idPath, resourceMan, runtimeInfo);
		return valuePortContainerInfo.getValuePortContainerPair().getRight().getValuePortGroupByType(HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK).getName();
	}
	
	public static void buildValuePortGroupForInteractiveTaskEventHandler(HAPManualBrick brick, HAPElementStructure eventDataElement, HAPDomainValueStructure valueStructureDomain) {
		Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair = Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer());
		
		//request
		Pair<HAPValuePort, HAPValuePort> requestValuePortPair = getOrCreateInteractiveRequestValuePort(valuePortContainerPair);
		
		Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure root = new HAPRootInStructure();
		root.setDefinition(eventDataElement);
		root.setName(HAPConstantShared.NAME_ROOT_EVENT);
		roots.add(root);
		
		String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
		requestValuePortPair.getLeft().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
		requestValuePortPair.getRight().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
	}

	public static HAPMatchers buildValuePortGroupForInteractiveTaskDataValidation(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide, HAPElementStructure dataElement, HAPDomainValueStructure valueStructureDomain, HAPRuntimeEnvironment runtimeEnv) {
		HAPMatchers out = null;
		
		String dataRootName = HAPConstantShared.NAME_ROOT_DATA;
		
		idPath = figureoutTaskPath(bundle, idPath, rootNameIfNotProvide);
		HAPManualBrick childBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(childBrick!=null) {
			Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair = Pair.of(childBrick.getOtherInternalValuePortContainer(), childBrick.getOtherExternalValuePortContainer());
			
			//request
			{
				Pair<HAPValuePort, HAPValuePort> requestValuePortPair = getOrCreateInteractiveRequestValuePort(valuePortContainerPair);
				HAPElementStructure dataEle = HAPUtilityValuePort.getStructureElementInValuePort(dataRootName, requestValuePortPair.getLeft(), valueStructureDomain);
				if(dataEle==null) {
					//if data root is not defined, then add it
					Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
					HAPRootInStructure root = new HAPRootInStructure();
					root.setDefinition(dataElement);
					root.setName(dataRootName);
					roots.add(root);
					
					String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
					requestValuePortPair.getLeft().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
					requestValuePortPair.getRight().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
				}
			}
			
			//result
			{
				String errorRootName = HAPConstantShared.NAME_ROOT_ERROR;
				Pair<HAPValuePort, HAPValuePort> resultValuePortPair = getOrCreateTaskInteractiveResultValuePort(HAPConstantShared.TASK_RESULT_FAIL, valuePortContainerPair);
				HAPElementStructure errorEle = HAPUtilityValuePort.getStructureElementInValuePort(errorRootName, resultValuePortPair.getLeft(), valueStructureDomain);
				if(errorEle==null) {
					Set<HAPRootInStructure> roots = new HashSet<HAPRootInStructure>();
					HAPRootInStructure root = new HAPRootInStructure();
					root.setDefinition(new HAPElementStructureLeafData(HAPParserCriteria.getInstance().parseCriteria("test.string;1.0.0")));
					root.setName(errorRootName);
					roots.add(root);
					
					String valueStructureId = valueStructureDomain.newValueStructure(roots, null, null, null);
					resultValuePortPair.getLeft().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
					resultValuePortPair.getRight().addValueStructureId(valueStructureId, HAPConstantShared.VALUESTRUCTURE_PRIORITY_IMPLIED);
				}
			}
			
		}
		
		//calculate matchers
		if(dataElement.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
			//external need different vlaue structuredomain
//			HAPInfoValuePortContainer valuePortContainerInfo = HAPUtilityBrick.getDescdentValuePortContainerInfo(bundle, idPath, runtimeEnv.getResourceManager(), runtimeEnv.getRuntime().getRuntimeInfo());
//			valuePortContainerInfo.getValuePortContainerPair()
			
//			Pair<HAPValuePort, HAPValuePort> requestValuePortPair = getOrCreateInteractiveRequestValuePort(Pair.of(taskWrapperBrick.getInternalValuePorts(), taskWrapperBrick.getExternalValuePorts()));
//			HAPElementStructure dataEle = HAPUtilityBrickValuePort.getStructureElementInValuePort(dataRootName, requestValuePortPair.getLeft(), valueStructureDomain);
//			out = HAPUtilityCriteria.isMatchable(((HAPElementStructureLeafData)dataElement).getCriteria(), ((HAPElementStructureLeafData)dataEle).getCriteria(), runtimeEnv.getDataTypeHelper());
		}
		
		return out;
		
	}

	public static void buildValuePortGroupForInteractiveTask(HAPWithExternalValuePort withExternalValuePort, HAPInteractiveTask taskInteractive, HAPDomainValueStructure valueStructureDomain) {
		buildValuePortGroupForInteractiveTask(Pair.of(null, withExternalValuePort.getExternalValuePorts()), taskInteractive, valueStructureDomain);
	}
	
	
	public static void buildValuePortGroupForInteractiveTask(HAPManualBrick brick, HAPInteractiveTask taskInteractive, HAPDomainValueStructure valueStructureDomain) {
		buildValuePortGroupForInteractiveTask(Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer()), taskInteractive, valueStructureDomain);
	}

	private static void buildValuePortGroupForInteractiveTask(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair, HAPInteractiveTask taskInteractive, HAPDomainValueStructure valueStructureDomain) {
		//request
		Pair<HAPValuePort, HAPValuePort> requestValuePortPair = getOrCreateInteractiveRequestValuePort(valuePortContainerPair);
		buildInteractiveRequestValuePort(requestValuePortPair, taskInteractive.getRequestParms(), valueStructureDomain);
		
		//result
		for(HAPInteractiveResultTask result : taskInteractive.getResult()) {
			Pair<HAPValuePort, HAPValuePort> resultValuePortPair = getOrCreateTaskInteractiveResultValuePort(result.getName(), valuePortContainerPair);
			buildTaskInteractiveResultValuePort(resultValuePortPair, result, valueStructureDomain);
		}
	}

	public static void buildValuePortGroupForInteractiveExpression(HAPManualBrick brick, HAPInteractiveExpression expressionInteractive, HAPDomainValueStructure valueStructureDomain) {
		buildValuePortGroupForInteractiveExpression(Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer()), expressionInteractive, valueStructureDomain);
	}

	private static void buildValuePortGroupForInteractiveExpression(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair, HAPInteractiveExpression expressionInteractive, HAPDomainValueStructure valueStructureDomain) {
		//request
		Pair<HAPValuePort, HAPValuePort> requestValuePortPair = getOrCreateInteractiveRequestValuePort(valuePortContainerPair);
		buildInteractiveRequestValuePort(requestValuePortPair, expressionInteractive.getRequestParms(), valueStructureDomain);
		
		//result
		Pair<HAPValuePort, HAPValuePort> resultValuePortPair = getOrCreateExpressionInteractiveResultValuePort(valuePortContainerPair);
		buildExpressionInteractiveResultValuePort(resultValuePortPair, expressionInteractive.getResult(), valueStructureDomain);
	}

	private static Pair<HAPValuePort, HAPValuePort> getOrCreateInteractiveRequestValuePort(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair){
		return HAPUtilityValuePort.getOrCreateValuePort(
				valuePortContainerPair, 
				HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK, 
				HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST, 
				HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST,
				Pair.of(HAPConstantShared.IO_DIRECTION_OUT, HAPConstantShared.IO_DIRECTION_IN));
	}
	
	private static Pair<HAPValuePort, HAPValuePort> getOrCreateTaskInteractiveResultValuePort(String resultName, Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair){
		return HAPUtilityValuePort.getOrCreateValuePort(
				valuePortContainerPair, 
				HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK, 
				HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, 
				buildResultValuePortName(resultName),
				Pair.of(HAPConstantShared.IO_DIRECTION_IN, HAPConstantShared.IO_DIRECTION_OUT));
	}
	
	private static Pair<HAPValuePort, HAPValuePort> getOrCreateExpressionInteractiveResultValuePort(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair){
		return HAPUtilityValuePort.getOrCreateValuePort(
				valuePortContainerPair, 
				HAPConstantShared.VALUEPORTGROUP_TYPE_INTERACTIVETASK, 
				HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, 
				HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT,
				Pair.of(HAPConstantShared.IO_DIRECTION_IN, HAPConstantShared.IO_DIRECTION_OUT));
	}
	
	
	private static void buildInteractiveRequestValuePort(Pair<HAPValuePort, HAPValuePort> valuePortPair, List<HAPRequestParmInInteractive> requestParms, HAPDomainValueStructure valueStructureDomain) {
		Set<HAPRootInStructure> requestRoots = new HashSet<HAPRootInStructure>();
		Map<String, HAPData> defaultValue = new LinkedHashMap<String, HAPData>(); 
		for(HAPRequestParmInInteractive parm : requestParms) {
			HAPRootInStructure root = null;
			if(parm.getCriteria()!=null) {
				root = new HAPRootInStructure(new HAPElementStructureLeafData(parm.getCriteria()), parm);
			}
			else {
				root = new HAPRootInStructure(new HAPElementStructureLeafValue(), parm);
			}
			requestRoots.add(root);
			defaultValue.put(parm.getName(), parm.getDefaultValue());
		}
		String requestVSId = valueStructureDomain.newValueStructure(requestRoots, defaultValue, null, null);
		
		//request value port -- internal
		if(valuePortPair.getLeft()!=null) {
			valuePortPair.getLeft().addValueStructureId(requestVSId);
		}
		
		//request value port -- external
		if(valuePortPair.getRight()!=null) {
			valuePortPair.getRight().addValueStructureId(requestVSId);
		}
	}


	private static void buildTaskInteractiveResultValuePort(Pair<HAPValuePort, HAPValuePort> valuePortPair, HAPInteractiveResultTask interactiveResult, HAPDomainValueStructure valueStructureDomain) {
		Set<HAPRootInStructure> outputRoots = new HashSet<HAPRootInStructure>();
		for(HAPResultElementInInteractiveTask element : interactiveResult.getOutput()) {
			HAPRootInStructure root = null;
			if(element.getCriteria()!=null) {
				root = new HAPRootInStructure(new HAPElementStructureLeafData(element.getCriteria()), element);
			}
			else {
				root = new HAPRootInStructure(new HAPElementStructureLeafValue(), element);
			}
			
			outputRoots.add(root);
		}
		String resultVSId = valueStructureDomain.newValueStructure(outputRoots, null, null, null);
		
		if(valuePortPair.getLeft()!=null) {
			valuePortPair.getLeft().addValueStructureId(resultVSId);
		}
		if(valuePortPair.getRight()!=null) {
			valuePortPair.getRight().addValueStructureId(resultVSId);
		}
	}

	private static String buildResultValuePortName(String resultName) {
		return HAPUtilityNamingConversion.cascadeComponents(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT, resultName, HAPConstantShared.SEPERATOR_PREFIX);
	}

	private static void buildExpressionInteractiveResultValuePort(Pair<HAPValuePort, HAPValuePort> valuePortPair, HAPInteractiveResultExpression interactiveResult, HAPDomainValueStructure valueStructureDomain) {
		//result value structure
		Set<HAPRootInStructure> resultRoots = new HashSet<HAPRootInStructure>();
		HAPRootInStructure resultRoot = new HAPRootInStructure(new HAPElementStructureLeafData(interactiveResult.getDataCriteria()), null);
		resultRoot.setName(HAPConstantShared.NAME_ROOT_RESULT);
		resultRoots.add(resultRoot);
		String resultVSId = valueStructureDomain.newValueStructure(resultRoots, null, null, null);
		
		if(valuePortPair.getLeft()!=null) {
			valuePortPair.getLeft().addValueStructureId(resultVSId);
		}
		if(valuePortPair.getRight()!=null) {
			valuePortPair.getRight().addValueStructureId(resultVSId);
		}
	}
	
}
