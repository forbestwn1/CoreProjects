package com.nosliw.data.core.service.interfacee;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdFactory;

public class HAPUtilityServiceInterface {

	public static HAPEntityOrReference parseInterface(Object content) {
		HAPEntityOrReference out = null;
		if(content instanceof String) {
			out = HAPResourceIdFactory.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, content);
		}
		else {
			HAPInfoServiceInterface serviceInterfaceInfo = new HAPInfoServiceInterface();
			serviceInterfaceInfo.buildObject(content, HAPSerializationFormat.JSON);
			out = serviceInterfaceInfo;
		}
		return out;
	}
	
}
