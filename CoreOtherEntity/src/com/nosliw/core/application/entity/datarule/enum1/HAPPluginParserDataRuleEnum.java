package com.nosliw.core.application.entity.datarule.enum1;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.data.HAPUtilityData;

public class HAPPluginParserDataRuleEnum implements HAPPluginParserDataRule{

	@Override
	public HAPDataRule parse(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		
		String enumCode = (String)jsonObj.opt(HAPDataRuleEnumCode.ENUMCODE);
		if(enumCode!=null) {
			HAPDataRuleEnumCode codeEnum = new HAPDataRuleEnumCode();
			codeEnum.setEnumCode(enumCode);
			return codeEnum;
		}
		else{
			HAPDataRuleEnumData dataEnum = new HAPDataRuleEnumData();
			JSONArray dataArray = jsonObj.optJSONArray(HAPDataRuleEnumData.DATASET);
			if(dataArray!=null) {
				for(int i=0; i<dataArray.length(); i++) {
					dataEnum.addData(HAPUtilityData.buildDataWrapperFromObject(dataArray.get(i)));
				}
			}
			return dataEnum;
		}
	}

}
