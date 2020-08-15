package com.nosliw.data.core.story.design;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPStory;

@HAPEntityWithAttribute
public class HAPQuestionnaire extends HAPSerializableImp{

	@HAPAttribute
	public static final String QUESTIONS = "questions";

	@HAPAttribute
	public static final String ANSWERS = "answers";

	private HAPQuestionGroup m_questions;
	
	private Map<String, HAPAnswer> m_answers;
	
	private HAPStory m_story;

	public HAPQuestionnaire() {
		this.m_answers = new LinkedHashMap<String, HAPAnswer>();
	}
	
	public void setQuestionGroup(HAPQuestionGroup questions) {
		questions.setId("0");
		this.m_questions = questions;    
	}
	
	public void addAnswer(HAPAnswer answer) {
		this.m_answers.put(answer.getQuestionId(), answer);
		answer.setStory(m_story);
	}

	public void setStory(HAPStory story) {
		this.m_story = story;
		for(HAPAnswer answer : this.m_answers.values()) {
			answer.setStory(story);
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject answersObject = jsonObj.optJSONObject(ANSWERS);
		if(answersObject!=null) {
			for(Object key : answersObject.keySet()) {
				HAPAnswer answer = new HAPAnswer();
				answer.buildObject(answersObject.getJSONObject((String)key), HAPSerializationFormat.JSON);
				this.addAnswer(answer);
			}
		}
		JSONObject questionsObj = jsonObj.optJSONObject(QUESTIONS);
		if(questionsObj!=null) {
			this.m_questions = new HAPQuestionGroup();
			this.m_questions.buildObject(questionsObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(QUESTIONS, HAPJsonUtility.buildJson(this.m_questions, HAPSerializationFormat.JSON));
		jsonMap.put(ANSWERS, HAPJsonUtility.buildJson(this.m_answers, HAPSerializationFormat.JSON));
	}

}
