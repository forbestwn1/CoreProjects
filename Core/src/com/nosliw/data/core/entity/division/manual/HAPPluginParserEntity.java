package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPIdEntityType;

public interface HAPPluginParserEntity {

	HAPIdEntityType getEntityType();
	
	HAPManualEntity parse(Object obj, HAPSerializationFormat format, HAPContextParse parseContext);

}
