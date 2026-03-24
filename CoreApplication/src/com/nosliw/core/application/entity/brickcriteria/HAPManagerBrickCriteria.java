package com.nosliw.core.application.entity.brickcriteria;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.facade.HAPCriteriaBrickFacade;
import com.nosliw.core.application.entity.brickcriteria.facade.task.HAPRestrainBrickTypeFacadeTaskInterface;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@Component
public class HAPManagerBrickCriteria {

	@Autowired
	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPCriteriaBrick parseCriteria(Object obj) {
		HAPCriteriaBrick out = null;
		
		if(obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			String criteriaType = (String)jsonObj.opt(HAPCriteriaBrick.CRITERIATYPE);
			if(criteriaType.equals(HAPConstantShared.BRICKTYPECRITERIA_TYPE_FACADE)) {
				out = HAPCriteriaBrickFacade.parse(jsonObj, this.m_dataRuleMan);
			}

			//parse restrain
			if(out!=null) {
				Object restrainObj = jsonObj.opt(HAPCriteriaBrick.RESTRAIN);
				if(restrainObj!=null) {
					if(restrainObj instanceof JSONObject) {
						out.getRestains().add(parseBrickTypeFacadeRestrain((JSONObject)restrainObj, m_dataRuleMan));
					}
					else if(restrainObj instanceof JSONArray) {
						JSONArray restrainArray = (JSONArray)restrainObj;
						for(int i=0; i<restrainArray.length(); i++) {
							out.addRestrain(parseBrickTypeFacadeRestrain(restrainArray.getJSONObject(i), m_dataRuleMan));
						}
					}
				}
			}
		}
		
		return out;
	}
	
	public static HAPRestrainBrick parseBrickTypeFacadeRestrain(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPRestrainBrick out = null;
		
		String type = jsonObj.getString(HAPRestrainBrick.TYPE);
		
		switch(type) {
		case HAPConstantShared.BRICKTYPECRITERIA_RESTRAIN_TASKINTERFACE:
			out = HAPRestrainBrickTypeFacadeTaskInterface.parse(jsonObj, dataRuleMan);
			break;
		}
		return out;
	}
}
