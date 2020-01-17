package com.nosliw.data.core.event;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.process.HAPWithProcessTask;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPDefinitionPollTask extends HAPSerializableImp implements HAPWithProcessTask{

	@HAPAttribute
	public static String INPUT = "input";
	
	private Map<String, HAPData> m_input;
	
	private HAPDefinitionWrapperTask<String> m_process;
	
	@Override
	public HAPDefinitionWrapperTask<String> getProcess() {  return this.m_process;   }
	
	@Override
	public void setProcess(HAPDefinitionWrapperTask<String> processTask) { this.m_process = processTask; }
	
	public Map<String, HAPData> getInput(){  return this.m_input;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		HAPDataUtility.buildDataWrapperMapFromJson(jsonObj.optJSONObject(INPUT));
		HAPUtilityProcess.parseWithProcessTask(this, jsonObj);
		return true;  
	}
}
