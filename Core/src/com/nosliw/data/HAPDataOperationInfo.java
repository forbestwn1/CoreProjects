package com.nosliw.data;

import java.util.Map;

public interface HAPDataOperationInfo {

	String getName();
	
	Map<String, HAPDataOperationParmInfo> getParmsInfo();
	
	HAPDataOperationOutInfo getOutputInfo();
	
}
