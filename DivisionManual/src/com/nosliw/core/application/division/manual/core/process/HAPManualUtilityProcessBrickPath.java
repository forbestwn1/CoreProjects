package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.path.HAPUtilityPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.brick.HAPUtilityBrickPath;
import com.nosliw.core.application.dynamic.HAPInputDynamic;
import com.nosliw.core.application.dynamic.HAPInputDynamicSingle;

public class HAPManualUtilityProcessBrickPath {

	public static void normalizeBrickPath(HAPIdBrickInBundle handlerIdInBundle, HAPManualContextProcessBrick processContext) {
		handlerIdInBundle.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), false, processContext.getCurrentBundle()).toString());
	}

	public static void processComplexBrickNormalizeBrickPath(HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).normalizeBrickPath(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPBrick complexBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				((HAPManualPluginProcessorBlockImp)processContext.getManualBrickManager().getBlockProcessPlugin(complexBrick.getBrickType())).postNormalizeBrickPath(path, processContext);
			}

		}, null);

		HAPManualUtilityBrickTraverse.traverseTree(processContext, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				if(path.isEmpty()) {
					return true;
				}
				
				if(HAPUtilityBundle.getBrickFullPathInfo(path).getPath().isEmpty()) {
					return true;
				}
				
				HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(bundle, path);
				if(attr.getValueWrapper().getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
					HAPWrapperValueOfReferenceResource resourceIdWrapper = (HAPWrapperValueOfReferenceResource)attr.getValueWrapper();
					for(HAPInputDynamic taskRef : resourceIdWrapper.getDynamicTaskInput().getDyanmicTaskReference().values()) {
						switch(taskRef.getType()) {
						case HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE:
							HAPInputDynamicSingle simpleDynamicTask = (HAPInputDynamicSingle)taskRef;
							HAPIdBrickInBundle taskIdRef = simpleDynamicTask.getTaskId();
							taskIdRef.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(taskIdRef.getIdPath()), processContext.getRootBrickName(), false, processContext.getCurrentBundle()).toString());
							taskIdRef.setRelativePath(HAPUtilityPath.fromAbsoluteToRelativePath(taskIdRef.getIdPath(), path.toString()));
							simpleDynamicTask.setTaskId(taskIdRef);
							break;
						}
					}
				}
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
			}

		}, null);

	}
	
}
