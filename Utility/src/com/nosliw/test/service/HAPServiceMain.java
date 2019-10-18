package com.nosliw.test.service;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.service.provide.HAPQueryService;
import com.nosliw.data.core.service.provide.HAPResultService;

public class HAPServiceMain {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPQueryService query = new HAPQueryService();
		query.setServiceId("TestProcessService");
		HAPResultService result = runtimeEnvironment.getServiceManager().execute(query, null);
		System.out.println(result.toString());
	}

}
