package com.nosliw.service.test.template2;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.service.HAPExecutableService;
import com.nosliw.core.application.service.HAPProviderService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("serviceParm1");
		HAPData parm2 = parms.get("serviceParm2");
		
		HAPData outData = new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "1234567890");
		
		
		output.put("outputInService", outData);
		return HAPUtilityService.generateSuccessResult(output);
	}

}
