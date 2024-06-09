package com.nosliw.core.application.common.interactive;

import java.util.List;

public interface HAPInteractiveExpression extends HAPInteractive{

	List<HAPRequestParmInInteractive> getRequestParms();
	
	HAPResultInInteractiveExpression getResult();

}
