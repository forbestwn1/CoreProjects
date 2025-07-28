package com.nosliw.service.test.simpleoutput.refinterface;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.service.HAPExecutableServiceImp;
import com.nosliw.core.application.service.HAPProviderService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;

public class HAPServiceImp extends HAPExecutableServiceImp implements HAPProviderService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		
		this.printParms(parms);
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("simpleOutput1", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output1_"+System.currentTimeMillis()));
		output.put("simpleOutput2", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output2_"+System.currentTimeMillis()));
		output.put("simpleOutput3", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output3_"+System.currentTimeMillis()));
		output.put("simpleOutput4", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output4_"+System.currentTimeMillis()));
		output.put("simpleOutput5", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output5_"+System.currentTimeMillis()));
		output.put("simpleOutput6", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), "output6_"+System.currentTimeMillis()));
		
		this.printOutput(output);
		return HAPUtilityService.generateSuccessResult(output);
	}

}
