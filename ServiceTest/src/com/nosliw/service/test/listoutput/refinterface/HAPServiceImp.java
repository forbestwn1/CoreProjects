package com.nosliw.service.test.listoutput.refinterface;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.domain.common.interactive.HAPResultInteractive;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPProviderService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		HAPData parm1 = parms.get("parm1");
		HAPData parm2 = parms.get("parm2");
		
		int num = (Integer)parm2.getValue();
		String strValue = (String)parm1.getValue();
		JSONArray outArray = new JSONArray();
		for(int i=0; i<num; i++) {
			JSONObject attr1Obj = HAPUtilityData.createJSONData("test.string;1.0.0", num+"_"+strValue+"_attr1");
			JSONObject attr2Obj = HAPUtilityData.createJSONData("test.string;1.0.0", num+"_"+strValue+"_attr2");
			JSONObject attr3Obj = HAPUtilityData.createJSONData("test.string;1.0.0", num+"_"+strValue+"_attr3");
			
			Map<String, JSONObject> eleObj = new LinkedHashMap<String, JSONObject>();
			eleObj.put("attribute1", attr1Obj);
			eleObj.put("attribute2", attr2Obj);
			eleObj.put("attribute3", attr3Obj);
			
			outArray.put(HAPUtilityData.createJSONData("test.map;1.0.0", eleObj));
		}
		
		output.put("listOutput", new HAPDataWrapper(new HAPDataTypeId("test.array;1.0.0"), outArray));
		return HAPUtilityService.generateSuccessResult(output);
	}

}
