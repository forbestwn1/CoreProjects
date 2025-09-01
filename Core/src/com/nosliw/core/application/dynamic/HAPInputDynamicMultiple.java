package com.nosliw.core.application.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPInputDynamicMultiple extends HAPInputDynamic{

	@HAPAttribute
	public final static String TASKIDS = "taskIds"; 
	
	private List<HAPIdBrickInBundle> m_taskIds;
	
	public HAPInputDynamicMultiple() {
		this.m_taskIds = new ArrayList<HAPIdBrickInBundle>();
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_MULTIPLE;  }

	public List<HAPIdBrickInBundle> getTaskIds() {    return this.m_taskIds;     }
	public void addTaskId(HAPIdBrickInBundle taskId) {    this.m_taskIds.add(taskId);      }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray taskIdJsonArray = jsonObj.getJSONArray(TASKIDS);
		for(int i=0; i<taskIdJsonArray.length(); i++) {
			HAPIdBrickInBundle taskId = new HAPIdBrickInBundle();
			taskId.buildObject(taskIdJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
			this.addTaskId(taskId);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASKIDS, HAPManagerSerialize.getInstance().toStringValue(m_taskIds, HAPSerializationFormat.JSON));
	}

}
