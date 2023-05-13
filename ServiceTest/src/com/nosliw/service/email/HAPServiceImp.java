package com.nosliw.service.email;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.interactive.HAPResultInteractive;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService{

//	https://gtaa-fl-prod.azureedge.net/api/flights/list?type=ARR&day=today&useScheduleTimeOnly=false

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		InputStream inputStream = HAPUtilityFile.getInputStreamOnClassPath(this.getClass(), "mockdata.js");
		return HAPUtilityService.readServiceResult(inputStream, "success");
	}
}
