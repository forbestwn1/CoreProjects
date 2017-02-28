package com.nosliw.data.expression;

import java.util.Map;

public class HAPOperandDataOperation implements HAPOperand{

	private HAPOperand m_baseData;

	//operation name
	protected String m_operation;
	//operation parms
	protected Map<String, HAPOperand> m_parms;
	
	public HAPOperandDataOperation(HAPOperand baseData, String operation, Map<String, HAPOperand> parms){
		this.m_baseData = baseData;
		this.m_operation = operation;
		this.m_parms = parms;
	}

}
