package com.nosliw.core.application.service;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAManagerServiceInterface {

	public HAPBrickServiceInterface getServiceInterface(HAPIdServcieInterface id) {
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + id.getId() + ".res";
		String content = HAPUtilityFile.readFile(new File(fileName));
		
		HAPBrickServiceInterface out = new HAPBrickServiceInterface();
		out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
		
		return out;
	}
	
}
