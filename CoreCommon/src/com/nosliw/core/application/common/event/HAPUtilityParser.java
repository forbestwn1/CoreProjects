package com.nosliw.core.application.common.event;

import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPStructureImp;
import com.nosliw.core.application.common.structure.HAPUtilityParserStructure;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPUtilityParser {

	public static HAPEventDefinition parseEventDefinition(Object obj, HAPManagerDataRule dataRuleManager) {
		HAPEventDefinition out = new HAPEventDefinition();
		
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			out.buildEntityInfoByJson(jsonObj);
			Object dfObj = jsonObj.opt(HAPEventDefinition.DATADEFINITION);
			if(dfObj!=null) {
				HAPStructure eventDataDef = new HAPStructureImp();
				HAPUtilityParserStructure.parseStuctureJson(dfObj, eventDataDef, dataRuleManager);
				out.setDataDefinition(eventDataDef);
			}
		}
		
        return out;
	}
	
}
