package com.nosliw.data.core.runtime;

import java.util.HashSet;
import java.util.Set;

public abstract class HAPRuntimeTask {

	private String m_id;
	
	private Object m_result;
	
	private Set<HAPRunTaskEventListener> m_eventListeners;
	
	public HAPRuntimeTask(){
		this.m_eventListeners = new HashSet<HAPRunTaskEventListener>();
	}

	//event
	public void registerListener(HAPRunTaskEventListener listener){  this.m_eventListeners.add(listener);  }
	public void trigueSuccessEvent(){
		for(HAPRunTaskEventListener listener : this.m_eventListeners){
			listener.success(this);
		}
	}

	//id
	public void setId(String id){  this.m_id = id; }
	public String getTaskId(){	return "Task__" + this.getTaskType() + "__" + this.m_id;	}
	
	//result
	public void setResult(Object result){ this.m_result = result;  }
	public Object getResult(){  return this.m_result;   }

	//success
	public void success(Object data){
		this.setResult(data);
		this.doSuccess();
		this.trigueSuccessEvent();
	}
	
	//override method
	protected void doSuccess(){}
	abstract public String getTaskType();
	abstract public HAPRuntimeTask execute(HAPRuntime runtime);
}
