package com.nosliw.service.test.simpleoutput.refinterface;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPProviderService;
import com.nosliw.data.core.service.definition.HAPResultService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("parm1");
		HAPData parm2 = parms.get("parm2");
		
		HAPData out1Data = new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), parm1.getValue()+"_"+parm2.getValue());
		HAPData out2Data = new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), parm2.getValue()+"_"+parm1.getValue());
		
		output.put("simpleOutput1", out1Data);
		output.put("simpleOutput2", out2Data);
		return HAPUtilityService.generateSuccessResult(output);
	}

}
