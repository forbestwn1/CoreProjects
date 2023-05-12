package com.nosliw.data.core.service.definition;

import java.util.Map;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.common.interactive.HAPResultInteractive;

//entity that answer service call
public interface HAPExecutableService {

	HAPResultInteractive execute(Map<String, HAPData> parms);
	
}
