package com.nosliw.core.application.division.story.design.wizard;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryWithAlias;

@HAPEntityWithAttribute
public abstract class HAPStoryQuestion extends HAPEntityInfoImp implements HAPStoryWithAlias{

	@HAPAttribute
	public static final String QUESTION = "question";

	@HAPAttribute
	public static final String TYPE = "type";

	private String m_question;
	
	public HAPStoryQuestion() {}
	
	public HAPStoryQuestion(String question) {
		this.m_question = question;
	}
	
	abstract public String getType(); 
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_question = (String)jsonObj.opt(QUESTION);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(QUESTION, this.m_question);
		jsonMap.put(TYPE, this.getType());
	}

	public static HAPStoryQuestion parseQuestion(JSONObject jsonObj) {
		HAPStoryQuestion out = null;
		String type = jsonObj.getString(TYPE);
		if(HAPConstantShared.STORYDESIGN_QUESTIONTYPE_GROUP.equals(type)) {
			out = new HAPStoryQuestionGroup();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		else if(HAPConstantShared.STORYDESIGN_QUESTIONTYPE_ITEM.equals(type)) {
			out = new HAPStoryQuestionItem();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
}
