package com.nosliw.service.pearsonflight;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.common.interactive.HAPResultInteractive;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		
		String flight = "matchName";
		HAPData flightData = parms.get("flight");
		if(flightData!=null)	flight = (String)flightData.getValue();
		InputStream inputStream = HAPUtilityFile.getInputStreamOnClassPath(this.getClass(), "mockdata.js");
		return HAPUtilityService.readServiceResult(inputStream, flight);
	}
}
