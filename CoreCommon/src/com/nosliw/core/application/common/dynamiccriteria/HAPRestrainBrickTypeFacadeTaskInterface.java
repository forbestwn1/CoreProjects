package com.nosliw.core.application.common.dynamiccriteria;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

public class HAPRestrainBrickTypeFacadeTaskInterface extends HAPSerializableImp implements HAPRestrainBrickTypeFacade{

	public final static String INTERFACE = "interface"; 
	
	private HAPInteractiveTask m_interactiveTaskInterface;
	
	@Override
	public String[] isValid(HAPBrick brick) {
		return null;
	}

	public HAPInteractiveTask getTaskInteractiveInterface() {
		return this.m_interactiveTaskInterface;
	}

	@Override
	public String getType() {
		return HAPRestrainBrickTypeFacade.TYPE_RESTRAIN_TASK_INTERFACE;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;

		m_interactiveTaskInterface = new HAPInteractiveTask();
		m_interactiveTaskInterface.buildObject(jsonObj.getJSONObject(INTERFACE), HAPSerializationFormat.JSON);
		
		return true;
	}
}
