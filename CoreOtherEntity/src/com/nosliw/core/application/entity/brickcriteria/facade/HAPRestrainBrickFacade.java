package com.nosliw.core.application.entity.brickcriteria.facade;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.entity.brickcriteria.facade.task.HAPRestrainBrickTypeFacadeTaskInterface;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public interface HAPRestrainBrickFacade extends HAPSerializable{

	public final static String TYPE = "type"; 

	public final static String TYPE_RESTRAIN_TASK_INTERFACE = "taskInterface";
	
	String getType();
	
	String[] isValid(HAPBrick brick);
	
	public static HAPRestrainBrickFacade parseBrickTypeFacadeRestrain(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPRestrainBrickFacade out = null;
		
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case TYPE_RESTRAIN_TASK_INTERFACE:
			out = HAPRestrainBrickTypeFacadeTaskInterface.parse(jsonObj, dataRuleMan);
			break;
		}
		return out;
	}
}
