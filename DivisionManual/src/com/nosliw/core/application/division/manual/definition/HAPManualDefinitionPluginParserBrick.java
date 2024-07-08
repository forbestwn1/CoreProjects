package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPManualDefinitionPluginParserBrick {

	HAPIdBrickType getBrickType();
	
	HAPManualDefinitionBrick parse(Object obj, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext);

	HAPManualDefinitionBrick newBrick();
}
