package com.nosliw.core.application.brick.ui.uicontent;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockComplexUICustomerTag  extends HAPBrick, HAPWithUIContent, HAPWithUIId{

	@HAPAttribute
	public static final String UITAGID = "uiTagId";
	@HAPAttribute
	public static final String SCRIPTRESOURCEID = "scriptResourceId";
	@HAPAttribute
	public static final String BASE = "base";

	
	
	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";


}
