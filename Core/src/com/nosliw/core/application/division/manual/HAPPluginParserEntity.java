package com.nosliw.core.application.division.manual;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginParserEntity {

	HAPIdBrickType getEntityType();
	
	HAPManualEntity parse(Object obj, HAPSerializationFormat format, HAPContextParse parseContext);

}
