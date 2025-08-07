package com.nosliw.core.application.division.manual.brick.container;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockContainerList extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockContainerList() {
		super(HAPEnumBrickType.CONTAINERLIST_100, HAPManualBrickContainerList.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBrickContainerList containerDef = (HAPManualDefinitionBrickContainerList)blockPair.getLeft();
		HAPManualBrickContainerList containerExe = (HAPManualBrickContainerList)blockPair.getRight();
		containerExe.getSort().addAll(containerDef.getSort());
	}

}
