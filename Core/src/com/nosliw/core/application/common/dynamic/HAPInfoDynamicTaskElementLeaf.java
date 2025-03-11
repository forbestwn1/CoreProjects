package com.nosliw.core.application.common.dynamic;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamicTaskElementLeaf extends HAPInfoDynamicTaskElement{

	@HAPAttribute
	public final static String TASKINTERFACE = "taskInterface"; 
	
	private HAPInteractiveTask m_taskInterface;
	
	public HAPInfoDynamicTaskElementLeaf() {}
	
	public HAPInfoDynamicTaskElementLeaf(HAPInteractiveTask taskInterface) {
		this.m_taskInterface = taskInterface;
	}

	public HAPInteractiveTask getTaskInterface() {
		return this.m_taskInterface;
	}

	@Override
	public HAPInfoDynamicTaskElement getChild(String childName) {   return null;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_taskInterface =  new HAPInteractiveTask();
		this.m_taskInterface.buildObject(jsonObj.getJSONObject(TASKINTERFACE), HAPSerializationFormat.JSON);
		return true;
	}
}
