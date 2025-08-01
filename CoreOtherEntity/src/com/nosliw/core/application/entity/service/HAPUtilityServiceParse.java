package com.nosliw.core.application.entity.service;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;

public class HAPUtilityServiceParse {

	public static HAPBlockInteractiveInterfaceTask parseTaskInterfaceInterfaceBlock(JSONObject jsonObj) {
		JSONObject serviceInterfaceJsonObj = jsonObj.optJSONObject(HAPWithBlockInteractiveTask.TASKINTERFACE);
		if(serviceInterfaceJsonObj==null) {
			serviceInterfaceJsonObj = jsonObj;
		}
		HAPBlockInteractiveInterfaceTaskImp interfaceBlock = new HAPBlockInteractiveInterfaceTaskImp();
		HAPInteractiveTask taskInterface = new HAPInteractiveTask();
		taskInterface.buildObject(serviceInterfaceJsonObj, HAPSerializationFormat.JSON);
		interfaceBlock.setValue(taskInterface);
		return interfaceBlock;
	}
	
}
