package com.nosliw.core.application.entity.brickcriteria.facade.task;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.entity.brickcriteria.HAPRestrainBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPRestrainBrickTypeFacadeTaskInterface extends HAPRestrainBrick{

	public final static String INTERFACE = "interface"; 
	
	private HAPInteractiveTask m_interactiveTaskInterface;
	
	public HAPRestrainBrickTypeFacadeTaskInterface() {
		super(HAPConstantShared.BRICKTYPECRITERIA_RESTRAIN_TASKINTERFACE);
	}
	
	public HAPInteractiveTask getTaskInteractiveInterface() {	return this.m_interactiveTaskInterface;	}
	public void setTaskInteractiveInterface(HAPInteractiveTask taskInterface) {	this.m_interactiveTaskInterface = taskInterface;	}

	public static HAPRestrainBrickTypeFacadeTaskInterface parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPRestrainBrickTypeFacadeTaskInterface out = new HAPRestrainBrickTypeFacadeTaskInterface();
		out.setTaskInteractiveInterface(HAPInteractiveTask.parse(jsonObj.getJSONObject(INTERFACE), dataRuleMan));
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;

		m_interactiveTaskInterface = new HAPInteractiveTask();
		m_interactiveTaskInterface.buildObject(jsonObj.getJSONObject(INTERFACE), HAPSerializationFormat.JSON);
		
		return true;
	}
}
