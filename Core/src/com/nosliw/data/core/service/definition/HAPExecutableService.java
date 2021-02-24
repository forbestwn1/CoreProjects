package com.nosliw.data.core.service.definition;

import java.util.Map;

import com.nosliw.data.core.data.HAPData;

//entity that answer service call
public interface HAPExecutableService {

	HAPResultService execute(Map<String, HAPData> parms);
	
}
