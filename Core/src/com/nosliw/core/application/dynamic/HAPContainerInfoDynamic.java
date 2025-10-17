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
public class HAPContainerInfoDynamic extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPInfoDynamic> m_elements;

	public HAPContainerInfoDynamic() {
		this.m_elements = new LinkedHashMap<String, HAPInfoDynamic>();
	}
	
	public void addElement(HAPInfoDynamic ele) {
		this.m_elements.put(ele.getName(), ele);
	}

	public HAPInfoDynamic getDescent(String path) {
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPInfoDynamic out = this.m_elements.get(complexPath.getRoot());
		
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
	
	public static HAPContainerInfoDynamic parse(Object json, HAPManagerDataRule dataRuleMan) {
		HAPContainerInfoDynamic out = new HAPContainerInfoDynamic();
		if(json instanceof JSONArray) {
			parseElements(out, (JSONArray)json, dataRuleMan);
		}
		else if(json instanceof JSONObject) {
			parseElements(out, ((JSONObject)json).optJSONArray(ELEMENT), dataRuleMan);
		}
		return out;
	}

	private static void parseElements(HAPContainerInfoDynamic out, JSONArray jsonArray, HAPManagerDataRule dataRuleMan) {
		if(jsonArray!=null) {
			for(int i=0; i<jsonArray.length(); i++) {
				out.addElement(HAPInfoDynamic.parse(jsonArray.get(i), dataRuleMan));
			}
		}
	}
}
