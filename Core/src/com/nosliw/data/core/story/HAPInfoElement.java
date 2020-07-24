package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoElement extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ELEMENTID = "elementId";

	private HAPIdElement m_eleId;
	
	public HAPInfoElement() {}
	
	public HAPInfoElement(HAPIdElement eleId) {
		this.setElementId(eleId);
	}
	
	public HAPIdElement getElementId() {    return this.m_eleId;     }
	
	private void setElementId(HAPIdElement eleId) {
		this.m_eleId = eleId;
		this.setId(eleId.toStringValue(HAPSerializationFormat.LITERATE));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		HAPIdElement eleId = new HAPIdElement();
		eleId.buildObject(jsonObj.getJSONObject(ELEMENTID), HAPSerializationFormat.JSON);
		this.setElementId(eleId);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTID, HAPJsonUtility.buildJson(this.m_eleId, HAPSerializationFormat.JSON));
	}
}
