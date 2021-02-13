package com.nosliw.data.core.component;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;

@HAPEntityWithAttribute
public interface HAPWithService {

	@HAPAttribute
	public static String SERVICEUSE = "serviceUse";

	HAPDefinitionServiceUse getService(String name);
	
	Set<String> getAllServices();
	
	void addService(HAPDefinitionServiceUse service);
	
}
