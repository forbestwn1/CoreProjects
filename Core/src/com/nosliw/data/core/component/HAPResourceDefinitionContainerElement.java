package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritable;

public interface HAPResourceDefinitionContainerElement extends HAPEntityInfoWritable{

	public static final String TYPE_REFERENCE = "reference";
	
	public static final String TYPE_ENTITY = "entity";
	
	String getType();

	HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement();
	
}
