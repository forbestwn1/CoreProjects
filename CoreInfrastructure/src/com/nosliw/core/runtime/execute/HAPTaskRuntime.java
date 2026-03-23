package com.nosliw.core.runtime.execute;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;

public abstract class HAPTaskRuntime {

	private String m_id;
	
	//result
	private HAPServiceData m_result;
	
	private Set<HAPRunTaskEventListener> m_eventListeners;
	
	public HAPTaskRuntime(){
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

	//data type for result
	public Class getResultDataType() {return null;}
	
	//success
	public void finish(HAPServiceData serviceData){
		HAPServiceData sd = this.processResult(serviceData);
		this.setResult(sd);
		this.doFinish();
		this.trigueSuccessEvent();
	}

	//override method
	protected HAPServiceData processResult(HAPServiceData serviceData) {   return serviceData;   }
	protected void doFinish(){}
	abstract public String getTaskType();
	abstract public HAPTaskRuntime execute(HAPExecutorRuntime runtime);
}
