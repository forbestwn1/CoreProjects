package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPParserChange;

@HAPEntityWithAttribute
public class HAPDesignStep extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String CHANGES = "changes";

	@HAPAttribute
	public static final String QUESTIONAIRE = "questionaire";

	private HAPStory m_story;
	
	private List<HAPChangeItem> m_changes;

	private HAPQuestionnaire m_questionaire;

	HAPDesignStep(HAPStory story) {
		this.m_changes = new ArrayList<HAPChangeItem>();
		this.m_questionaire = new HAPQuestionnaire();
		this.m_story = story;
	}
	
	public List<HAPChangeItem> getChanges(){    return this.m_changes;    }
	
	public void addChange(HAPChangeItem changeItem) {
		this.m_changes.add(changeItem);	
	}
	
	public void addChanges(List<HAPChangeItem> changeItems) {
		for(HAPChangeItem changeItem : changeItems) {
			this.addChange(changeItem);
		}
	}

	public void addAnswers(List<HAPAnswer> answers) {
		for(HAPAnswer answer : answers) {
			this.addAnswer(answer);
		}
	}
	
	public void addAnswer(HAPAnswer answer) {
		this.m_questionaire.addAnswer(answer);
	}
	
	public void setQuestion(HAPQuestionGroup question) {
		//solidate ref
		question.processAlias(this.m_story);
		
		question.setId("0");
		this.m_questionaire.setQuestionGroup(question); 
	}
		
	public HAPQuestionnaire getQuestionair() {   return this.m_questionaire;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);

		JSONArray changeArray = jsonObj.getJSONArray(CHANGES);
		for(int i=0; i<changeArray.length(); i++) {
			JSONObject changeJson = changeArray.getJSONObject(i);
			HAPChangeItem changeItem = HAPParserChange.parseChangeItem(changeJson);
			this.addChange(changeItem);
		}
		
		JSONObject questionaireObj = jsonObj.optJSONObject(QUESTIONAIRE);
		this.m_questionaire.buildObject(questionaireObj, HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHANGES, HAPJsonUtility.buildJson(this.m_changes, HAPSerializationFormat.JSON));
		jsonMap.put(QUESTIONAIRE, HAPJsonUtility.buildJson(this.m_questionaire, HAPSerializationFormat.JSON));
	}
}
