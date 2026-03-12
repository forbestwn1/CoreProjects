package com.nosliw.service.test.basic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.common.interactive.HAPResultInteractiveTask;
import com.nosliw.core.application.entity.service.HAPExecutableService;
import com.nosliw.core.application.entity.service.HAPProviderService;
import com.nosliw.core.application.entity.service.HAPUtilityService;
import com.nosliw.core.data.HAPData;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractiveTask execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("serviceParm1");
		
		output.put("outputInService1", parm1);
		output.put("outputInService2", parm1);
		return HAPUtilityService.generateSuccessResult(output);
	}

}

