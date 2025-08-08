package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.HAPManualProviderBrickInfoImp;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

@Component
public class HAPManualProviderBrickInfoDataExpressionLibrary extends HAPManualProviderBrickInfoImp{

	public HAPManualProviderBrickInfoDataExpressionLibrary(HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		super(manualBrickMan, brickMan);
	}
	
	@Override
	public HAPIdBrickType getBrickTypeId() {  return HAPEnumBrickType.DATAEXPRESSIONLIB_100;   }

	@Override
	protected HAPManualInfoBrickType newBrickTypeInfo() {   return new HAPManualInfoBrickType(true);  }

	@Override
	protected HAPManualDefinitionPluginParserBrick newBrickParser() {
		return new HAPManualPluginParserBlockDataExpressionLibrary(this.getManualBrickManager(), this.getBrickManager());
	}

	@Override
	protected HAPManualPluginProcessorBrick newBrickProcessor() {   return new HAPManualPluginProcessorBlockDataExpressionLibrary();  }

}
