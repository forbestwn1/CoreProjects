package com.nosliw.core.runtime;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;

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

	//data type for result
	public Class getResultDataType() {return null;}
	
	//success
	public void finish(HAPServiceData serviceData){
		if(serviceData.isSuccess()){
			if(this.getResultDataType()==HAPData.class) {
				//if result is data, then convert json object to data
				JSONObject dataJson = (JSONObject)serviceData.getData();
				HAPData data = HAPUtilityData.buildDataWrapperFromJson(dataJson);
				serviceData.setData(data);
			}
		}
		this.setResult(serviceData);
		this.doFinish();
		this.trigueSuccessEvent();
	}

	//override method
	protected void doFinish(){}
	abstract public String getTaskType();
	abstract public HAPRuntimeTask execute(HAPRuntime runtime);
}
