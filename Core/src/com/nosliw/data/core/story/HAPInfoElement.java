package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoElement extends HAPEntityInfoImp implements HAPWithAlias{

	@HAPAttribute
	public static final String ELEMENTREF = "elementRef";

	private HAPReferenceElementWrapper m_eleRef;
	
	public HAPInfoElement() {}
	
	public HAPInfoElement(HAPReferenceElement eleRef) {
		this.m_eleRef = new HAPReferenceElementWrapper(eleRef);
	}
	
	public HAPIdElement getElementId() {  return this.m_eleRef.getElementId(); }
	
	@Override
	public void processAlias(HAPStory story) {
		this.m_eleRef.processAlias(story);
	}

	public HAPInfoElement cloneElementInfo() {
		HAPInfoElement out = new HAPInfoElement();
		this.cloneToEntityInfo(out);
		out.m_eleRef = this.m_eleRef.cloneElementReferenceWrapper();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_eleRef = new HAPReferenceElementWrapper();
		this.m_eleRef.buildObject(jsonObj.getJSONObject(ELEMENTREF), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTREF, HAPJsonUtility.buildJson(this.m_eleRef, HAPSerializationFormat.JSON));
	}
}
