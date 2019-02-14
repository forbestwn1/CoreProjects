package com.nosliw.data.core.script.context;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;

public class HAPUtilityContextInfo {

	public static String getRelativeConnectionValue(HAPInfo info) {
		return (String)info.getValue(HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION, HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL);
	}

	
}
