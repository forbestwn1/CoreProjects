package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryParserChange;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryDesignStep extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String CHANGES = "changes";

	@HAPAttribute
	public static final String QUESTIONAIRE = "questionaire";

	private HAPStoryStory m_story;
	
	private List<HAPStoryChangeItem> m_changes;

	private HAPStoryQuestionnaire m_questionaire;

	HAPStoryDesignStep(HAPStoryStory story) {
		this.m_changes = new ArrayList<HAPStoryChangeItem>();
		this.m_questionaire = new HAPStoryQuestionnaire();
		this.m_story = story;
	}
	
	public List<HAPStoryChangeItem> getChanges(){    return this.m_changes;    }
	
	public void addChange(HAPStoryChangeItem changeItem) {
		this.m_changes.add(changeItem);	
	}
	
	public void addChanges(List<HAPStoryChangeItem> changeItems) {
		for(HAPStoryChangeItem changeItem : changeItems) {
			this.addChange(changeItem);
		}
	}

	public void addAnswers(List<HAPStoryAnswer> answers) {
		for(HAPStoryAnswer answer : answers) {
			this.addAnswer(answer);
		}
	}
	
	public void addAnswer(HAPStoryAnswer answer) {
		this.m_questionaire.addAnswer(answer);
	}
	
	public void setQuestion(HAPStoryQuestionGroup question) {
		//solidate ref
		question.processAlias(this.m_story);
		
		question.setId("0");
		this.m_questionaire.setQuestionGroup(question); 
	}
		
	public HAPStoryQuestionnaire getQuestionair() {   return this.m_questionaire;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);

		JSONArray changeArray = jsonObj.getJSONArray(CHANGES);
		for(int i=0; i<changeArray.length(); i++) {
			JSONObject changeJson = changeArray.getJSONObject(i);
			HAPStoryChangeItem changeItem = HAPStoryParserChange.parseChangeItem(changeJson);
			this.addChange(changeItem);
		}
		
		JSONObject questionaireObj = jsonObj.optJSONObject(QUESTIONAIRE);
		this.m_questionaire.buildObject(questionaireObj, HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHANGES, HAPUtilityJson.buildJson(this.m_changes, HAPSerializationFormat.JSON));
		jsonMap.put(QUESTIONAIRE, HAPUtilityJson.buildJson(this.m_questionaire, HAPSerializationFormat.JSON));
	}
}
