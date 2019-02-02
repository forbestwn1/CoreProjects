package com.nosliw.data.core.service.provide;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPExecutableService {

	HAPResultService execute(Map<String, HAPData> parms);
	
}
