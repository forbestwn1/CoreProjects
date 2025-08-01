package com.nosliw.core.application.entity.service;

import java.util.Map;

import com.nosliw.core.data.HAPData;

//entity that answer service call
public interface HAPExecutableService {

	HAPResultInteractive execute(Map<String, HAPData> parms);
	
}
