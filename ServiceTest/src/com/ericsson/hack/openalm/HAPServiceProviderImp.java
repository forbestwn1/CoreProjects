package com.ericsson.hack.openalm;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPProviderService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;

public class HAPServiceProviderImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms) {
		String query = (String)parms.get("query").getValue();
		InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(this.getClass(), "mingle_"+query);
		return HAPUtilityService.readServiceResult(inputStream, "success");
	}

}