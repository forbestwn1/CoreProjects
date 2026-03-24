package com.nosliw.core.application.dynamic;

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
public class HAPDynamicExecuteInputContainer extends HAPSerializableImp{

	@HAPAttribute
	public final static String ELEMENT = "element"; 
	
	private Map<String, HAPDynamicExecuteInputItem> m_dynamicInputs;

	public HAPDynamicExecuteInputContainer() {
		this.m_dynamicInputs = new LinkedHashMap<String, HAPDynamicExecuteInputItem>();
	}
	
	public Map<String, HAPDynamicExecuteInputItem> getDyanmicTaskReference() {		return this.m_dynamicInputs;	}
	public void addDynamicTaskReference(HAPDynamicExecuteInputItem ref) {   this.m_dynamicInputs.put(ref.getName(), ref);     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPManagerSerialize.getInstance().toStringValue(m_dynamicInputs, HAPSerializationFormat.JSON)); 
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray dynamicTaskRefsArray = jsonObj.optJSONArray(ELEMENT);
		for(int i=0; i<dynamicTaskRefsArray.length(); i++) {
			this.addDynamicTaskReference(HAPDynamicExecuteInputItem.parse(dynamicTaskRefsArray.getJSONObject(i)));
		}
		return true;  
	}
}
