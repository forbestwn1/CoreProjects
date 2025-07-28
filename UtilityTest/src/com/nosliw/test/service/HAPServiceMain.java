package com.nosliw.test.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.service.HAPQueryService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPServiceMain {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPQueryService query = new HAPQueryService();
		query.setServiceId("TestProcessService");
		
		Map<String, HAPData> input = new LinkedHashMap<String, HAPData>();
		input.put("fromVar", HAPUtilityData.buildDataWrapper("#test.integer;1.0.0___7"));
		input.put("toVar", HAPUtilityData.buildDataWrapper("#test.integer;1.0.0___8"));
//		input.put("baseVar", HAPDataUtility.buildDataWrapper("#test.string;1.0.0___helloworld"));
		
		HAPResultInteractive result = runtimeEnvironment.getServiceManager().execute(query, input);
		System.out.println(result.toString());
	}

}
