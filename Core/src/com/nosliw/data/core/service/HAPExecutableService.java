package com.nosliw.data.core.service;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPExecutableService {

	Map<String, HAPData> execute(Map<String, HAPData> parms);
	
}
