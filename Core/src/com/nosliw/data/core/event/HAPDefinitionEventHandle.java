package com.nosliw.data.core.event;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.process.HAPWithProcessTask;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

//event process definition
public class HAPDefinitionEventHandle extends HAPSerializableImp implements HAPWithProcessTask{

	//how to process event(inputmapping, process, outputmapping)
	private HAPDefinitionWrapperTask<String> m_process; 
	
	@Override
	public HAPDefinitionWrapperTask<String> getProcess() {  return this.m_process;   }
	@Override
	public void setProcess(HAPDefinitionWrapperTask<String> processTask) {  this.m_process = processTask;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		HAPUtilityProcess.parseWithProcessTask(this, jsonObj);
		return true;  
	}
	
}
