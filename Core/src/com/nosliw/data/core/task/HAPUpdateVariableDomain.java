package com.nosliw.data.core.task;

import com.nosliw.data.core.expression.HAPExpressionUtility;

public class HAPUpdateVariableDomain implements HAPUpdateVariable{

	private String m_domain;
	
	public HAPUpdateVariableDomain(String domain) {
		this.m_domain = domain;
	}
	
	@Override
	public String getUpdatedVariable(String varName) {
		return HAPExpressionUtility.buildFullVariableName(this.m_domain, varName);
	}

}
