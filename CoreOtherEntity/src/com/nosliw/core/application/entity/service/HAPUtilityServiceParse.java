package com.nosliw.core.application.entity.service;

import org.json.JSONObject;

import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTaskImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPUtilityServiceParse {

	public static HAPBlockInteractiveInterfaceTask parseTaskInterfaceInterfaceBlock(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		JSONObject serviceInterfaceJsonObj = jsonObj.optJSONObject(HAPWithBlockInteractiveTask.TASKINTERFACE);
		if(serviceInterfaceJsonObj==null) {
			serviceInterfaceJsonObj = jsonObj;
		}
		HAPBlockInteractiveInterfaceTaskImp interfaceBlock = new HAPBlockInteractiveInterfaceTaskImp();
		HAPInteractiveTask taskInterface = HAPInteractiveTask.parse(serviceInterfaceJsonObj, dataRuleMan); 
		interfaceBlock.setValue(taskInterface);
		return interfaceBlock;
	}
	
}
