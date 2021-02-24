package com.nosliw.service.test.template3;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPProviderService;
import com.nosliw.data.core.service.definition.HAPResultService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("serviceParm1");
		
		output.put("outputInService1", parm1);
		output.put("outputInService2", parm1);
		return HAPUtilityService.generateSuccessResult(output);
	}

}
