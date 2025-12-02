package com.nosliw.core.application.division.story.design.wizard;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElementWrapper;
import com.nosliw.core.application.division.story.HAPStoryStory;

public class HAPStoryQuestionItem extends HAPStoryQuestion{

	@HAPAttribute
	public static final String TARGETREF = "targetRef";

	@HAPAttribute
	public static final String ISMANDATORY = "isMandatory";

	private HAPStoryReferenceElementWrapper m_targetRef;
	
	private boolean m_isMandatory = false;
	
	public HAPStoryQuestionItem() {}
	
	public HAPStoryQuestionItem(String question, HAPStoryReferenceElement targetRef) {
		super(question);
		this.m_targetRef = new HAPStoryReferenceElementWrapper(targetRef);
	}

	public HAPStoryQuestionItem(String question, HAPStoryReferenceElement targetRef, boolean isMandatory) {
		this(question, targetRef);
		this.m_isMandatory = isMandatory;
	}

	@Override
	public void processAlias(HAPStoryStory story) {  this.m_targetRef.processAlias(story);  }

	@Override
	public String getType() {	return HAPConstantShared.STORYDESIGN_QUESTIONTYPE_ITEM;	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_targetRef = new HAPStoryReferenceElementWrapper();
		this.m_targetRef.buildObject(jsonObj.getJSONObject(TARGETREF), HAPSerializationFormat.JSON);
		
		this.m_isMandatory = jsonObj.getBoolean(ISMANDATORY);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETREF, HAPUtilityJson.buildJson(this.m_targetRef, HAPSerializationFormat.JSON));
		jsonMap.put(ISMANDATORY, this.m_isMandatory+"");
		typeJsonMap.put(ISMANDATORY, Boolean.class);
	}
}
