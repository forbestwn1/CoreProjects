package com.nosliw.data.core.structure;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityContextInfo {

	public static String getRelativeConnectionValue(HAPInfo info) {
		return (String)info.getValue(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION, HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL);
	}

	
}
