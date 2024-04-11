package com.nosliw.core.application.division.manual;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginParserBrick {

	HAPIdBrickType getBrickType();
	
	HAPManualBrick parse(Object obj, HAPSerializationFormat format, HAPManualContextParse parseContext);

}
