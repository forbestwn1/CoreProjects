package com.nosliw.data.core.domain.entity.service.interfacee;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAManagerServiceInterface {

	public HAPInfoServiceInterface getServiceInterface(HAPIdServcieInterface id) {
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + id.getId() + ".res";
		String content = HAPUtilityFile.readFile(new File(fileName));
		
		HAPInfoServiceInterface out = new HAPInfoServiceInterface();
		out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
		
		return out;
	}
	
}
