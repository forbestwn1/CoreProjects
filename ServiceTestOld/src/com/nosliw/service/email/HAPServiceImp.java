package com.nosliw.service.email;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.service.HAPExecutableService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.data.core.data.HAPData;

public class HAPServiceImp implements HAPExecutableService{

//	https://gtaa-fl-prod.azureedge.net/api/flights/list?type=ARR&day=today&useScheduleTimeOnly=false

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		InputStream inputStream = HAPUtilityFile.getInputStreamOnClassPath(this.getClass(), "mockdata.js");
		return HAPUtilityService.readServiceResult(inputStream, "success");
	}
}
