package com.nosliw.data.core.event;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.process1.HAPUtilityProcess;

public class HAPDefinitionPollTask extends HAPSerializableImp{
//implements HAPEmbededProcessTask{

	@HAPAttribute
	public static String INPUT = "input";
	
	private Map<String, HAPData> m_input;
	
	private HAPDefinitionWrapperTask<String> m_process;
	
	@Override
	public HAPDefinitionWrapperTask<String> getTask() {  return this.m_process;   }
	
	@Override
	public void setTask(HAPDefinitionWrapperTask<String> processTask) { this.m_process = processTask; }
	
	public Map<String, HAPData> getInput(){  return this.m_input;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_input = HAPUtilityData.buildDataWrapperMapFromJson(jsonObj.optJSONObject(INPUT));
		HAPUtilityProcess.parseWithProcessTask(this, jsonObj);
		return true;  
	}
}
