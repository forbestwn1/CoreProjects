package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.process.HAPWithProcessTask;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPHandlerLifecycle extends HAPEntityInfoWritableImp implements HAPWithProcessTask{

	private HAPDefinitionWrapperTask<String> m_process;

	public static HAPHandlerLifecycle newInstance(JSONObject jsonObj) {
		HAPHandlerLifecycle out = new HAPHandlerLifecycle();
		out.buildObjectByJson(jsonObj);
		return out;
	}
	
	@Override
	public HAPDefinitionWrapperTask<String> getProcess(){   return this.m_process;    }

	@Override
	public void setProcess(HAPDefinitionWrapperTask<String> processTask) {  this.m_process = processTask;  }

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
}
