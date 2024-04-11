package com.nosliw.core.application.service;

import java.util.Map;

import com.nosliw.data.core.data.HAPData;

//entity that answer service call
public interface HAPExecutableService {

	HAPResultInteractive execute(Map<String, HAPData> parms);
	
}
