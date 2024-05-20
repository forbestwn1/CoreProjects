package com.nosliw.core.application.service;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilitySerializeJson;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPManagerServiceInterface{
	
	private HAPManagerApplicationBrick m_brickManager;  
	
	public HAPManagerServiceInterface(HAPManagerApplicationBrick brickManager) {
		this.m_brickManager = brickManager;
	}
	
	public HAPBlockServiceInterface getServiceInterface(HAPIdServcieInterface id) {
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + id.getId() + ".res";
		String content = HAPUtilityFile.readFile(new File(fileName));
		return (HAPBlockServiceInterface)HAPUtilitySerializeJson.buildBrick(new JSONObject(content), HAPEnumBrickType.SERVICEINTERFACE_100, m_brickManager);
	}
}
