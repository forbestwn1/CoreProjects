package com.nosliw.core.application.uitag;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

public class HAPBlockUITagDefinition extends HAPBrickImp{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	
	
	
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
