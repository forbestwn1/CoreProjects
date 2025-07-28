package com.nosliw.service.test.basic.reference;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.service.HAPExecutableService;
import com.nosliw.core.application.service.HAPProviderService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.core.data.HAPData;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("serviceParm1");
		
		output.put("outputInService1", parm1);
		output.put("outputInService2", parm1);
		return HAPUtilityService.generateSuccessResult(output);
	}

}
