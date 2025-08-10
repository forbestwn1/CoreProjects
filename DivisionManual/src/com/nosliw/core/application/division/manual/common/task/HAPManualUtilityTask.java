package com.nosliw.core.application.division.manual.common.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveTaskValuePort;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.valueport.HAPInfoValuePortContainer;
import com.nosliw.core.application.valueport.HAPUtilityBrickValuePort;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

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
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTaskEventHandler(Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer()), eventDataElement, valueStructureDomain);
	}

	public static HAPMatchers buildValuePortGroupForInteractiveTaskDataValidation(HAPBundle bundle, HAPPath idPath, String rootNameIfNotProvide, HAPElementStructure dataElement, HAPDomainValueStructure valueStructureDomain) {
		HAPMatchers out = null;
		
		idPath = figureoutTaskPath(bundle, idPath, rootNameIfNotProvide);
		HAPManualBrick childBrick = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle, idPath, rootNameIfNotProvide);
		if(childBrick!=null) {
			HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTaskDataValidation(Pair.of(childBrick.getOtherInternalValuePortContainer(), childBrick.getOtherExternalValuePortContainer()), dataElement, valueStructureDomain);
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

	
	public static void buildValuePortGroupForInteractiveTask(HAPManualBrick brick, HAPInteractiveTask taskInteractive, HAPDomainValueStructure valueStructureDomain) {
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveTask(Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer()), taskInteractive, valueStructureDomain);
	}

	public static void buildValuePortGroupForInteractiveExpression(HAPManualBrick brick, HAPInteractiveExpression expressionInteractive, HAPDomainValueStructure valueStructureDomain) {
		HAPUtilityInteractiveTaskValuePort.buildValuePortGroupForInteractiveExpression(Pair.of(brick.getOtherInternalValuePortContainer(), brick.getOtherExternalValuePortContainer()), expressionInteractive, valueStructureDomain);
	}


}
