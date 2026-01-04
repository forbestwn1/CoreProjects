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
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionContainer extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPDynamicDefinitionItem> m_elements;

	public HAPDynamicDefinitionContainer() {
		this.m_elements = new LinkedHashMap<String, HAPDynamicDefinitionItem>();
	}
	
	public void addElement(HAPDynamicDefinitionItem ele) {
		this.m_elements.put(ele.getName(), ele);
	}

	public HAPDynamicDefinitionItem getDescent(String path) {
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPDynamicDefinitionItem out = this.m_elements.get(complexPath.getRoot());
		
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
	
	public static HAPDynamicDefinitionContainer parse(Object json, HAPDynamicDefinitionContainer dynamicInfo, HAPManagerDataRule dataRuleMan) {
		if(json instanceof JSONArray) {
			parseElements(dynamicInfo, (JSONArray)json, dataRuleMan);
		}
		else if(json instanceof JSONObject) {
			parseElements(dynamicInfo, ((JSONObject)json).optJSONArray(ELEMENT), dataRuleMan);
		}
		return dynamicInfo;
	}

	private static void parseElements(HAPDynamicDefinitionContainer out, JSONArray jsonArray, HAPManagerDataRule dataRuleMan) {
		if(jsonArray!=null) {
			for(int i=0; i<jsonArray.length(); i++) {
				out.addElement(HAPDynamicDefinitionItem.parse(jsonArray.get(i), dataRuleMan));
			}
		}
	}
}
