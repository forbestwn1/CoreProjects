package com.nosliw.data.core.process.activity;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPMappingResult;

public class HAPDefinitionActivityCallProcess extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";
	
	//referenced process
	private String m_processId;

	private HAPDefinitionProcess m_process;
	
	private HAPMappingResult m_resultMapping;
	
	@Override
	public String getType() {  return HAPConstant.ACTIVITY_TYPE_CALLPROCESS;	}

	
	
}
