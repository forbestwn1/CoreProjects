package com.nosliw.core.application.division.manual.brick.container;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockContainer extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockContainer() {
		super(HAPEnumBrickType.CONTAINER_100, HAPManualBrickContainer.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBrickContainer containerDef = (HAPManualDefinitionBrickContainer)blockPair.getLeft();
		HAPManualBrickContainer containerExe = (HAPManualBrickContainer)blockPair.getRight();
	}

}
