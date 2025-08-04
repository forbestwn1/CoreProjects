package com.nosliw.core.application.division.manual.core;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;

public interface HAPManualProviderBrickInfo {

	HAPIdBrickType getBrickTypeId();
	
	HAPManualDefinitionPluginParserBrick getBrickParser();
	
	HAPManualPluginProcessorBrick getBrickProcessor();
	
	HAPManualInfoBrickType getBrickTypeInfo();
	
}
