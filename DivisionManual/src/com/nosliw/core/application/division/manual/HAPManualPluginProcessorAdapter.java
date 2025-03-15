package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPResultBrickDescentValue;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualPluginProcessorAdapter extends HAPManualPluginProcessorBrick{

	public HAPManualPluginProcessorAdapter(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	//process
	public abstract void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef, HAPManualContextProcessAdapter processContext);

	public void normalizeBrickPath(HAPPath pathFromRoot, HAPManualContextProcessAdapter processContext) {}
	public void postNormalizeBrickPath(HAPPath pathFromRoot, HAPManualContextProcessAdapter processContext) {}
	
	protected HAPPath getRootPathForBaseBrick(HAPManualContextProcessAdapter processContext) {
		return processContext.getRootPathForBaseBrick();
	}

	protected HAPResultBrickDescentValue getBaseBrickResult(HAPManualContextProcessAdapter processContext) {
		return HAPUtilityBrick.getDescdentBrickResult(processContext.getCurrentBundle(), getRootPathForBaseBrick(processContext), processContext.getRootBrickName());
	}

	protected HAPIdBrickType getBaseBrickType(HAPManualContextProcessAdapter processContext) {
		HAPResultBrickDescentValue result = this.getBaseBrickResult(processContext);
		if(result.getBrick()!=null) {
			return result.getBrick().getBrickType();
		} else {
			return HAPUtilityBrickId.getBrickTypeIdFromResourceId(result.getResourceId());
		}
	}
	
	protected HAPPath getSecondBlockPath(HAPManualContextProcessAdapter processContext) {
		HAPPath out;
		HAPPath baseBlockPath = getRootPathForBaseBrick(processContext);
		HAPIdBrickType brickTypeId = this.getBaseBrickType(processContext);
		if(HAPManualUtilityTask.getBrickTaskType(brickTypeId, this.getManualBrickManager())==null){
			out = baseBlockPath.trimLast().getLeft();
		}
		else {
			out = baseBlockPath.trimLast().getLeft().trimLast().getLeft();
		}
		return out;
	}
}
