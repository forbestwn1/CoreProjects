package com.nosliw.core.application.common.interactive;

import java.util.List;

public interface HAPInteractiveTask extends HAPInteractive{

	List<HAPRequestParmInInteractive> getRequestParms();
	
	List<HAPInteractiveResultTask> getResults();

}
