package com.nosliw.core.application.service;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAManagerServiceInterface implements HAPPluginDivision{

	public HAPBrickServiceInterface getServiceInterface(HAPIdServcieInterface id) {
		String fileName = HAPSystemFolderUtility.getServiceInterfaceFolder() + id.getId() + ".res";
		String content = HAPUtilityFile.readFile(new File(fileName));
		
		HAPBrickServiceInterface out = new HAPBrickServiceInterface();
		out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
		
		return out;
	}

	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		HAPIdBrickType brickTypeId = brickId.getBrickTypeId();
		if(brickTypeId.equals(HAPEnumBrickType.SERVICEINTERFACE_100)) {
			HAPBundle bundle = new HAPBundle();
			bundle.setBrickWrapper(new HAPWrapperBrick(this.getServiceInterface(new HAPIdServcieInterface(brickId.getId()))));
			return bundle;
		}
		return null;
	}
	
}
