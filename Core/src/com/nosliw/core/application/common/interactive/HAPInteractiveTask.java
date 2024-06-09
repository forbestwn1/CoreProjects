package com.nosliw.core.application.common.interactive;

import java.util.List;
import java.util.Map;

public interface HAPInteractiveTask extends HAPInteractive{

	List<HAPRequestParmInInteractive> getRequestParms();
	
	Map<String, HAPResultInInteractiveTask> getResults();

}
