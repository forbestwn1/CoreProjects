package com.nosliw.test.cronjob;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.cronjob.HAPCronJobId;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.cronjob.HAPResourceIdCronJob;
import com.nosliw.data.core.imp.cronjob.HAPRuntimeCronJob;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPCronJob {

	public static void main(String[] args) {

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPManagerCronJob cronJobMan = runtimeEnvironment.getCronJobManager();
		
		HAPDataWrapper flightNum = new HAPDataWrapper(new HAPDataTypeId("test.string", "1.0.0"), "1234");
		HAPDataWrapper flightDate = new HAPDataWrapper(new HAPDataTypeId("test.date", "1.0.0"), Instant.now().toString());
		Map<String, HAPData> parms = new LinkedHashMap<String, HAPData>();
		parms.put("flightNumber", flightNum);
		parms.put("date", flightDate);
		
		HAPRuntimeCronJob runtime = new HAPRuntimeCronJob(cronJobMan);
		runtime.newJob(new HAPResourceIdCronJob(new HAPCronJobId("flightarrive")), parms);
		
	}
	
	
}
