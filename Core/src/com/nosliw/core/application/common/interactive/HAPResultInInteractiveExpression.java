package com.nosliw.core.application.common.interactive;

import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public class HAPResultInInteractiveExpression {

	private HAPDataTypeCriteria m_dataCriteria;
	
	public HAPResultInInteractiveExpression(HAPDataTypeCriteria dataCriteria) {
		this.m_dataCriteria = dataCriteria;
	}
	
	public HAPDataTypeCriteria getDataCriteria() {
		return this.m_dataCriteria;
	}

}
