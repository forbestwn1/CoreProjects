package com.nosliw.data.core.handler;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.process.HAPEmbededProcessTask;

@HAPEntityWithAttribute
public class HAPHandlerStepProcess extends HAPEmbededProcessTask implements HAPHandlerStep{

	@Override
	public String getHandlerStepType() {  return HAPConstantShared.HANDLERSTEP_TYPE_PROCESS;  }
	
	@Override
	public HAPHandlerStep cloneHandlerStep() {
		HAPHandlerStepProcess out = new HAPHandlerStepProcess();
		this.cloneToEmbededProcessTask(out);
		return out;
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STEPTYPE, this.getHandlerStepType());
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		return true;  
	}
	
}
