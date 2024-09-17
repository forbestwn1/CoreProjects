package com.nosliw.core.application.service;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

public class HAPUtilityServiceParse {

	public static HAPBlockInteractiveInterfaceTask parseServiceInterfaceBlock(JSONObject jsonObj) {
		JSONObject serviceInterfaceJsonObj = jsonObj.getJSONObject(HAPBlockServiceInterface.INTERFACE);
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
