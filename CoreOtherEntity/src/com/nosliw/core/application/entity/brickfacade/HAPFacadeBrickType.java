package com.nosliw.core.application.entity.brickfacade;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPFacadeBrickType extends HAPEntityInfo{

	public final static String TYPE = "type"; 
	
	public final static String TYPE_SIMPLE = "simpleFacade"; 
	
	String getType();

}
