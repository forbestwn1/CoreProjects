package com.nosliw.core.application.common.interactive;

import java.util.List;

import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public interface HAPInteractiveExpression {

	List<HAPRequestParmInInteractiveInterface> getRequestParms();
	
	HAPDataTypeCriteria getResult();

}
