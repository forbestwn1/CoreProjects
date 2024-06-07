package com.nosliw.core.application.common.interactive;

import java.util.List;
import java.util.Map;

public interface HAPInteractiveTask {

	List<HAPRequestParmInInteractiveInterface> getRequestParms();
	
	Map<String, HAPResultInInteractiveInterface> getResults();

}
