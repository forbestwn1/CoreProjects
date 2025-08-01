package com.nosliw.core.application.entity.service;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPManagerServiceInterface{
	
	public HAPManagerServiceInterface() {
	}
	
	public HAPBlockServiceInterface getServiceInterface(HAPIdServcieInterface id) {
		HAPBlockServiceInterfaceImp out = new HAPBlockServiceInterfaceImp();
		
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + id.getId() + ".json";
		String content = HAPUtilityFile.readFile(new File(fileName));

		JSONObject jsonObj = new JSONObject(content);

		//entity info
		out.buildEntityInfoByJson(jsonObj);
		
		//interface
		out.setTaskInteractiveInterface(HAPUtilityServiceParse.parseTaskInterfaceInterfaceBlock(jsonObj));
		
		return out;
	}
}
