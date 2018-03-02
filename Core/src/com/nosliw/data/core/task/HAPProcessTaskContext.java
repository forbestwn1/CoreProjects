package com.nosliw.data.core.task;

import java.util.ArrayList;
import java.util.List;

public class HAPProcessTaskContext {

	public static final String CONFIGURE_DISCOVERY = "discovery";
	
	//whether the processing success
	private boolean m_success;
	
	//error message
	private List<String> m_messages;
	
	public HAPProcessTaskContext(){
		this.clear();
	}
	
	public void clear(){
		this.m_success = true;
		this.m_messages = new ArrayList<String>();
	}
	
	public boolean isSuccess(){  return this.m_success;  }
	public void setFailure(String message){
		this.m_success = false;
		this.m_messages.add(message);
	}
	
	public List<String> getMessages(){  return this.m_messages;  }
	public void addMessage(String message){  this.m_messages.add(message);  }
}
