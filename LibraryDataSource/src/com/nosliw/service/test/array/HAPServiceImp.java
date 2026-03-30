package com.nosliw.service.test.array;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.common.interactive.HAPResultInteractiveTask;
import com.nosliw.core.application.entity.service.HAPExecutableService;
import com.nosliw.core.application.entity.service.HAPProviderService;
import com.nosliw.core.application.entity.service.HAPUtilityService;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractiveTask execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();

		HAPData parm1 = parms.get("serviceParm1");
		output.put("outputStringInService1", parm1);

		String dataStr = HAPUtilityFile.readFile(getClass(), "data.json");
		HAPData outParm1 = HAPUtilityData.buildDataWrapperFromJson(new JSONObject(dataStr));
		output.put("outputArrayInService1", outParm1);

		return HAPUtilityService.generateSuccessResult(output);
	}

}
