package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPEmbededProcessTaskImp  extends HAPEntityInfoWritableImp implements HAPEmbededProcessTask{

	private HAPDefinitionWrapperTask<String> m_process;

	@Override
	public HAPDefinitionWrapperTask<String> getTask(){   return this.m_process;    }

	@Override
	public void setTask(HAPDefinitionWrapperTask<String> processTask) {  this.m_process = processTask;  }

	@Override
	public String getProcess() {  return this.m_process.getTaskDefinition(); }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		HAPUtilityProcess.parseWithProcessTask(this, jsonObj);
		return true;  
	}
	
	protected void cloneToEmbededProcessTask(HAPEmbededProcessTaskImp task) {
		this.cloneToEntityInfo(task);
		if(this.m_process==null)   task.m_process = null;
		else task.m_process = this.m_process.clone();
	}
}
