package com.nosliw.data.core.expression;

public class HAPProcessVariablesContext {

	private boolean m_success;
	
	private String m_message;
	
	public HAPProcessVariablesContext(){
		this.clear();
	}
	
	public void clear(){
		this.m_success = true;
		this.m_message = null;
	}
	
	public boolean isSuccess(){  return this.m_success;  }
	public void setFailure(String message){
		this.m_success = false;
		this.m_message = message;
	}
	
	public String getMessage(){  return this.m_message;  }
	public void setMessage(String message){  this.m_message = message;  }
	
}
