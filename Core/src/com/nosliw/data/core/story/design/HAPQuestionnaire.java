package com.nosliw.data.core.story.design;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPQuestionnaire extends HAPSerializableImp{

	@HAPAttribute
	public static final String QUESTIONS = "questions";

	@HAPAttribute
	public static final String ANSWERS = "answers";

	private HAPQuestionGroup m_questions;
	
	private Map<String, HAPAnswer> m_answers;
	
	public HAPQuestionnaire() {
		this.m_answers = new LinkedHashMap<String, HAPAnswer>();
	}
	
	public void setQuestionGroup(HAPQuestionGroup questions) {
		questions.setId("0");
		this.m_questions = questions;    
	}
	
	public void addAnswer(HAPAnswer answer) {
		this.m_answers.put(answer.getQuestionId(), answer);
	}

	public Set<HAPAnswer> getAnswers(){   return new HashSet<HAPAnswer>(this.m_answers.values());     }
	
	public void clearAnswer() {   this.m_answers.clear();    }
	
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
