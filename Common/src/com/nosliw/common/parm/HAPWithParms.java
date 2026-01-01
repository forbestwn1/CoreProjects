package com.nosliw.common.parm;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithParms {

	@HAPAttribute
	public static String PARM = "parm";

	HAPParms getParms();
	
	void setParms(HAPParms parms);
	
}
