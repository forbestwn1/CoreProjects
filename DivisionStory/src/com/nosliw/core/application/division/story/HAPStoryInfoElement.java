package com.nosliw.core.application.division.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryInfoElement extends HAPEntityInfoImp implements HAPStoryWithAlias{

	@HAPAttribute
	public static final String ELEMENTREF = "elementRef";

	private HAPStoryReferenceElementWrapper m_eleRef;
	
	public HAPStoryInfoElement() {}
	
	public HAPStoryInfoElement(HAPStoryReferenceElement eleRef) {
		this.m_eleRef = new HAPStoryReferenceElementWrapper(eleRef);
	}
	
	public HAPStoryIdElement getElementId() {  return this.m_eleRef.getElementId(); }
	
	@Override
	public void processAlias(HAPStoryStory story) {
		this.m_eleRef.processAlias(story);
	}

	public HAPStoryInfoElement cloneElementInfo() {
		HAPStoryInfoElement out = new HAPStoryInfoElement();
		this.cloneToEntityInfo(out);
		out.m_eleRef = this.m_eleRef.cloneElementReferenceWrapper();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_eleRef = new HAPStoryReferenceElementWrapper();
		this.m_eleRef.buildObject(jsonObj.getJSONObject(ELEMENTREF), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTREF, HAPUtilityJson.buildJson(this.m_eleRef, HAPSerializationFormat.JSON));
	}
}
