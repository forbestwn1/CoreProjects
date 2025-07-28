package com.nosliw.core.application.common.dynamic;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInputDynamicTask extends HAPSerializableImp{

	@HAPAttribute
	public final static String DYNAMICTASK = "dynamicTask"; 
	
	private Map<String, HAPRefDynamicTask> m_dynamicTaskRefs;

	public HAPInputDynamicTask() {
		this.m_dynamicTaskRefs = new LinkedHashMap<String, HAPRefDynamicTask>();
	}
	
	public Map<String, HAPRefDynamicTask> getDyanmicTaskReference() {		return this.m_dynamicTaskRefs;	}
	public void addDynamicTaskReference(HAPRefDynamicTask ref) {   this.m_dynamicTaskRefs.put(ref.getName(), ref);     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DYNAMICTASK, HAPManagerSerialize.getInstance().toStringValue(m_dynamicTaskRefs, HAPSerializationFormat.JSON)); 
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray dynamicTaskRefsArray = jsonObj.optJSONArray(DYNAMICTASK);
		for(int i=0; i<dynamicTaskRefsArray.length(); i++) {
			this.addDynamicTaskReference(HAPRefDynamicTask.parse(dynamicTaskRefsArray.getJSONObject(i)));
		}
		return true;  
	}
}
