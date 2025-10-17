package com.nosliw.core.application.common.dynamiccriteria;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public interface HAPRestrainBrickTypeFacade extends HAPSerializable{

	public final static String TYPE = "type"; 

	public final static String TYPE_RESTRAIN_TASK_INTERFACE = "taskInterface";
	
	String getType();
	
	String[] isValid(HAPBrick brick);
	
	public static HAPRestrainBrickTypeFacade parseBrickTypeFacadeRestrain(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPRestrainBrickTypeFacade out = null;
		
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case TYPE_RESTRAIN_TASK_INTERFACE:
			out = HAPRestrainBrickTypeFacadeTaskInterface.parse(jsonObj, dataRuleMan);
			break;
		}
		return out;
	}
}
