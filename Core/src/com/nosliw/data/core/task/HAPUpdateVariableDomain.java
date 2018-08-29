package com.nosliw.data.core.task;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.expression.HAPExpressionUtility;

public class HAPUpdateVariableDomain implements HAPUpdateName{

	private String m_domain;
	
	public HAPUpdateVariableDomain(String domain) {
		this.m_domain = domain;
	}
	
	@Override
	public String getUpdatedName(String varName) {
		return HAPExpressionUtility.buildFullVariableName(this.m_domain, varName);
	}

}
