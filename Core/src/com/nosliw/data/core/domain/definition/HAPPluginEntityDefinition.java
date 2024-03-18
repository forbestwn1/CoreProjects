package com.nosliw.data.core.domain.definition;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

//plug in for entity definition in domain
//   how to parse json object for entity
public interface HAPPluginEntityDefinition {

	String getEntityType();
	
	boolean isComplexEntity();

	HAPManualBrick parse(Object obj, HAPSerializationFormat format);

	
	
	//new definition instance
	HAPIdEntityInDomain newInstance(HAPContextParser parserContext);
	
	//parse json to entity, and set entity to existing entity in domain
//	void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);
	void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPSerializationFormat format, HAPContextParser parserContext);

}
