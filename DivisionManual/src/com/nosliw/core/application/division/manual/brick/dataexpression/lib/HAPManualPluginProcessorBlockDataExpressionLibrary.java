package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockDataExpressionLibrary extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockDataExpressionLibrary.class);
	}

}
