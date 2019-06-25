package com.nosliw.service.soccer;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPProviderService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;

public class HAPServiceGetLineup implements HAPExecutableService, HAPProviderService{

	public static void main(String[] argus) throws Exception{
		HAPServiceGetLineup dataSource = new HAPServiceGetLineup();
		HAPResultService result = dataSource.execute(null);
		System.out.println(result);
	}
	
	@Override
	public HAPResultService execute(Map<String, HAPData> parms){
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("outputInService", new HAPDataWrapper(new HAPDataTypeId("test.array;1.0.0"), schoolArrayData));
		return HAPUtilityService.generateSuccessResult(output);
		return HAPUtilityService.generateSuccessResult(HAPPlayerLineupManager.getInstance().getLineup());
	}

}
