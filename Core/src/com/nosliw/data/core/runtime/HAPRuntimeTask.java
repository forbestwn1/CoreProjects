package com.nosliw.data.core.runtime;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;

public abstract class HAPRuntimeTask {

	private String m_id;
	
	//result
	private HAPServiceData m_result;
	
	private Set<HAPRunTaskEventListener> m_eventListeners;
	
	public HAPRuntimeTask(){
		this.m_eventListeners = new HashSet<HAPRunTaskEventListener>();
	}

	//event
	public void registerListener(HAPRunTaskEventListener listener){  this.m_eventListeners.add(listener);  }
	public void trigueSuccessEvent(){
		for(HAPRunTaskEventListener listener : this.m_eventListeners){
			listener.finish(this);
		}
	}

	//id
	public void setId(String id){  this.m_id = id; }
	public String getTaskId(){	return "Task__" + this.getTaskType() + "__" + this.m_id;	}
	
	//result
	public void setResult(HAPServiceData result){ this.m_result = result;  }
	public HAPServiceData getResult(){  return this.m_result;   }

	//success
	public void finish(HAPServiceData data){
		this.setResult(data);
		this.doFinish();
		this.trigueSuccessEvent();
	}

	//override method
	protected void doFinish(){}
	abstract public String getTaskType();
	abstract public HAPRuntimeTask execute(HAPRuntime runtime);
}
