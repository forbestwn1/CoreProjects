package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPReferenceElementWrapper;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPWithAlias;

public class HAPChangeItemModifyElement extends HAPChangeItem implements HAPWithAlias{

	@HAPAttribute
	public static final String TARGETELEMENTREF = "targetElementRef";

	private HAPReferenceElementWrapper m_targetElementRef;
	
	public HAPChangeItemModifyElement(String type) {
		super(type);
	}
	
	public HAPChangeItemModifyElement(String type, HAPReferenceElement targetElementRef) {
		this(type);
		this.m_targetElementRef = new HAPReferenceElementWrapper(targetElementRef);
	}
	
	public HAPIdElement getTargetElementId() {  return this.m_targetElementRef.getElementId(); } 

	@Override
	public void processAlias(HAPStory story) {	this.m_targetElementRef.processAlias(story);	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_targetElementRef = new HAPReferenceElementWrapper();
		this.m_targetElementRef.buildObject(jsonObj.getJSONObject(TARGETELEMENTREF), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETELEMENTREF, HAPUtilityJson.buildJson(this.m_targetElementRef, HAPSerializationFormat.JSON));
	}
}
