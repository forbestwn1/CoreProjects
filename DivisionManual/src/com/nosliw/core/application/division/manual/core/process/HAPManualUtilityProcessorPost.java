package com.nosliw.core.application.division.manual.core.process;

import java.util.Set;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualUtilityProcessorPost {

	public static void process(HAPManualContextProcessBrick processContext) {
		cleanupEmptyValueStructure(processContext);
	}
	
	private static void cleanupEmptyValueStructure(HAPManualContextProcessBrick processContext) {
		Set<String> vsIds = processContext.getCurrentBundle().getValueStructureDomain().cleanupEmptyValueStructure();

		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext.getCurrentBundle(), processContext.getRootBrickName(), new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPBundle bundle, HAPPath path, Object data) {
				HAPManualBrickImp brick = (HAPManualBrickImp)HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
				brick.getManualValueContext().cleanValueStucture(vsIds);
				brick.getOtherExternalValuePortContainer().cleanValueStucture(vsIds);
				brick.getOtherInternalValuePortContainer().cleanValueStucture(vsIds);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPBundle bundle, HAPPath path, Object data) {
			}

		}, processContext.getBrickManager(), null);
	}
	

	
}
