package com.nosliw.core.application.division.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElementWrapper;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryWithAlias;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryChangeItemModifyElement extends HAPStoryChangeItem implements HAPStoryWithAlias{

	@HAPAttribute
	public static final String TARGETELEMENTREF = "targetElementRef";

	private HAPStoryReferenceElementWrapper m_targetElementRef;
	
	public HAPStoryChangeItemModifyElement(String type) {
		super(type);
	}
	
	public HAPStoryChangeItemModifyElement(String type, HAPStoryReferenceElement targetElementRef) {
		this(type);
		this.m_targetElementRef = new HAPStoryReferenceElementWrapper(targetElementRef);
	}
	
	public HAPStoryIdElement getTargetElementId() {  return this.m_targetElementRef.getElementId(); } 

	@Override
	public void processAlias(HAPStoryStory story) {	this.m_targetElementRef.processAlias(story);	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_targetElementRef = new HAPStoryReferenceElementWrapper();
		this.m_targetElementRef.buildObject(jsonObj.getJSONObject(TARGETELEMENTREF), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETELEMENTREF, HAPUtilityJson.buildJson(this.m_targetElementRef, HAPSerializationFormat.JSON));
	}
}
