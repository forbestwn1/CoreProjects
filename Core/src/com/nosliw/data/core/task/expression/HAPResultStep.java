package com.nosliw.data.core.task.expression;

import com.nosliw.data.core.HAPData;

public class HAPResultStep {

	private HAPData m_data;
	
	private String m_variableName;
	
	private boolean m_exit;
	
	private String m_next;

	public static HAPResultStep createExitResult(HAPData data){
		HAPResultStep out = new HAPResultStep();
		out.m_data = data;
		out.m_exit = true;
		return out;
	}
	
	public static HAPResultStep createJumpResult(String next){
		HAPResultStep out = new HAPResultStep();
		out.m_next = next;
		out.m_exit = false;
		return out;
	}
	
	public static HAPResultStep createNextStepResult(HAPData data, String variableName){
		HAPResultStep out = new HAPResultStep();
		out.m_data = data;
		out.m_variableName = variableName;
		out.m_exit = false;
		return out;
	}
	
	public boolean isExit() {  return this.m_exit;  }
	public String getNext() {   return this.m_next;  }
	public HAPData getData() {   return this.m_data;    }
	public String getVariableName() {   return this.m_variableName;   }
}
