package com.nosliw.core.application.bricktypefacade;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPFacadeBrickType extends HAPSerializable{

	public final static String TYPE = "type"; 
	
	public final static String TYPE_SIMPLE = "simpleFacade"; 
	
	String getType();

}
