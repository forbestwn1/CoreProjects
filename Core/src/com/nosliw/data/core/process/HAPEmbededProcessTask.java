package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPEmbededProcessTask extends HAPSerializableImp{

	@HAPAttribute
	public static String PROCESS = "process";

	private HAPDefinitionWrapperTask<String> m_wrapperTask;

	public HAPDefinitionWrapperTask<String> getTask(){   return this.m_wrapperTask;    }

	public void setTask(HAPDefinitionWrapperTask<String> processTask) {  this.m_wrapperTask = processTask;  }

	public String getProcess() {  return this.m_wrapperTask.getTaskDefinition(); }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		this.m_wrapperTask.buildJsonMap(jsonMap, typeJsonMap, PROCESS);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_wrapperTask = new HAPDefinitionWrapperTask<String>();
		this.m_wrapperTask.buildObj(jsonObj, jsonObj.optString(HAPEmbededProcessTask.PROCESS));
		return true;  
	}
	
	protected void cloneToEmbededProcessTask(HAPEmbededProcessTask task) {
		if(this.m_wrapperTask==null)   task.m_wrapperTask = null;
		else task.m_wrapperTask = this.m_wrapperTask.clone();
	}
}
