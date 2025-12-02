package com.nosliw.core.application.division.story.design.wizard;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryQuestionnaire extends HAPSerializableImp{

	@HAPAttribute
	public static final String QUESTIONS = "questions";

	@HAPAttribute
	public static final String ANSWERS = "answers";

	private HAPStoryQuestionGroup m_questions;
	
	private Map<String, HAPStoryAnswer> m_answers;
	
	public HAPStoryQuestionnaire() {
		this.m_answers = new LinkedHashMap<String, HAPStoryAnswer>();
	}
	
	public void setQuestionGroup(HAPStoryQuestionGroup questions) {
		questions.setId("0");
		this.m_questions = questions;    
	}
	
	public void addAnswer(HAPStoryAnswer answer) {
		this.m_answers.put(answer.getQuestionId(), answer);
	}

	public Set<HAPStoryAnswer> getAnswers(){   return new HashSet<HAPStoryAnswer>(this.m_answers.values());     }
	
	public void clearAnswer() {   this.m_answers.clear();    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject answersObject = jsonObj.optJSONObject(ANSWERS);
		if(answersObject!=null) {
			for(Object key : answersObject.keySet()) {
				HAPStoryAnswer answer = new HAPStoryAnswer();
				answer.buildObject(answersObject.getJSONObject((String)key), HAPSerializationFormat.JSON);
				this.addAnswer(answer);
			}
		}
		JSONObject questionsObj = jsonObj.optJSONObject(QUESTIONS);
		if(questionsObj!=null) {
			this.m_questions = new HAPStoryQuestionGroup();
			this.m_questions.buildObject(questionsObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(QUESTIONS, HAPUtilityJson.buildJson(this.m_questions, HAPSerializationFormat.JSON));
		jsonMap.put(ANSWERS, HAPUtilityJson.buildJson(this.m_answers, HAPSerializationFormat.JSON));
	}
}
