package com.nosliw.service.email;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService{

//	https://gtaa-fl-prod.azureedge.net/api/flights/list?type=ARR&day=today&useScheduleTimeOnly=false

	@Override
	public HAPResultService execute(Map<String, HAPData> parms) {
		InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(this.getClass(), "mockdata.js");
		return HAPUtilityService.readServiceResult(inputStream, "success");
	}
}
