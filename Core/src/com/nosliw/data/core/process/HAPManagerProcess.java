package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;

public interface HAPManagerProcess {

	HAPServiceData executeProcess(String process, HAPDefinitionProcessSuite suit, Map<String, HAPData> input);

	HAPServiceData executeProcess(String process, String suitId, Map<String, HAPData> input);
	
	HAPServiceData executeProcess(HAPExecutableProcess process, Map<String, HAPData> input);
	
}
