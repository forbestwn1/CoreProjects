package com.nosliw.core.application.dynamic;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoDynamicTask extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPInfoDynamicTaskElement> m_elements;

	public HAPInfoDynamicTask() {
		this.m_elements = new LinkedHashMap<String, HAPInfoDynamicTaskElement>();
	}
	
	public void addElement(HAPInfoDynamicTaskElement ele) {
		this.m_elements.put(ele.getName(), ele);
	}

	public HAPInfoDynamicTaskElement getDescent(String path) {
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPInfoDynamicTaskElement out = this.m_elements.get(complexPath.getRoot());
		
		for(String seg : complexPath.getPathSegs()) {
			out = out.getChild(seg);
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPManagerSerialize.getInstance().toStringValue(m_elements, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		if(json instanceof JSONArray) {
			this.parseElements((JSONArray)json);
		}
		else if(json instanceof JSONObject) {
			this.parseElements(((JSONObject)json).optJSONArray(ELEMENT));
		}
		return true;  
	}

	private void parseElements(JSONArray jsonArray) {
		if(jsonArray!=null) {
			for(int i=0; i<jsonArray.length(); i++) {
				this.addElement(HAPInfoDynamicTaskElement.parse(jsonArray.get(i)));
			}
		}
	}
}
