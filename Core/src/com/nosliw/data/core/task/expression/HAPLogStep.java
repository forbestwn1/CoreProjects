package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPLog;

public class HAPLogStep extends HAPLog{

	private String m_stepName;
	
	private String m_stepId;
	
	private int m_stepIndex;
	
	private Map<String, HAPData> m_parms;
	
	private Map<String, HAPData> m_referencedData;
	
	private HAPResultStep m_results;
	
	public HAPLogStep(HAPExecutableStep step, Map<String, HAPData> parms, Map<String, HAPData> referencedData) {
		this.m_stepName = step.getName();
		this.m_stepIndex = step.getIndex();
		this.m_parms = new LinkedHashMap<String, HAPData>(parms);
		this.m_referencedData = new LinkedHashMap<String, HAPData>(referencedData);
		
	}
	
	public void setResult(HAPResultStep result) {	this.m_results = result;	}
}
