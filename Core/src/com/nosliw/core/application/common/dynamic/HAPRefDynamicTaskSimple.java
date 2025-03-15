package com.nosliw.core.application.common.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPRefDynamicTaskSimple extends HAPRefDynamicTask{

	@HAPAttribute
	public final static String TASKID = "taskId"; 
	
	private HAPIdBrickInBundle m_taskId;
	
	public HAPRefDynamicTaskSimple() {	}
	
	public HAPRefDynamicTaskSimple(HAPIdBrickInBundle taskId) {
		this.m_taskId = taskId;
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_SIMPLE;  }

	public HAPIdBrickInBundle getTaskId() {    return this.m_taskId;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject taskIdJsonObj = jsonObj.getJSONObject(TASKID);
		this.m_taskId = new HAPIdBrickInBundle();
		this.m_taskId.buildObject(taskIdJsonObj, HAPSerializationFormat.JSON);
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKID, this.m_taskId.toStringValue(HAPSerializationFormat.JSON));
	}

}
