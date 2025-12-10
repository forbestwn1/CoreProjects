package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickReference;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.dynamic.HAPInputDynamic;
import com.nosliw.core.application.dynamic.HAPInputDynamicSingle;

public class HAPManualUtilityProcessBrickPath {

	public static void normalizeBrickReferenceInBundle(HAPIdBrickInBundle brickIdInBundle, HAPPath basePath, boolean processEnd, HAPManualContextProcessBrick processContext) {
		HAPUtilityBrickReference.normalizeBrickReferenceInBundle(brickIdInBundle, basePath.getPath(), processEnd, processContext.getRootBrickName(), processContext.getCurrentBundle());
	}
	
//	public static void normalizeBrickPath(HAPIdBrickInBundle brickIdInBundle, HAPManualContextProcessBrick processContext) {
//		brickIdInBundle.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(brickIdInBundle.getIdPath()), processContext.getRootBrickName(), false, processContext.getCurrentBundle()).toString());
//	}

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
							normalizeBrickReferenceInBundle(simpleDynamicTask.getTaskId(), path, false, processContext);
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
