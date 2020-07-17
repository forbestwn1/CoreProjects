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

@HAPEntityWithAttribute
public class HAPChangeBatch extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String INITIALCHANGE = "initialChange";

	@HAPAttribute
	public static final String CHANGES = "changes";

	@HAPAttribute
	public static final String QUESTION = "question";

	private HAPStory m_story;
	
	private List<HAPChangeItem> m_initialChange;
	
	private List<HAPChangeItem> m_changes;

	private HAPQuestionGroup m_question;

	public HAPChangeBatch() {
		this.m_initialChange = new ArrayList<HAPChangeItem>();
		this.m_changes = new ArrayList<HAPChangeItem>();
	}
	
	public void addChange(HAPChangeItem changeItem) {
		changeItem.setStory(m_story);
		this.m_initialChange.add(changeItem);	
	}
	
	public void addInitialChange(HAPChangeItem changeItem) {
		changeItem.setStory(m_story);
		this.m_changes.add(changeItem);	
	}
	
	public void setQuestion(HAPQuestionGroup question) {	this.m_question = question; 	}
	
	public void setStory(HAPStory story) {    
		this.m_story = story;
		for(HAPChangeItem change : this.m_initialChange)   change.setStory(m_story);
		for(HAPChangeItem change : this.m_changes)  change.setStory(m_story);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		
		JSONArray initChangeArray = jsonObj.getJSONArray(INITIALCHANGE);
		for(int i=0; i<initChangeArray.length(); i++) {
			JSONObject changeJson = initChangeArray.getJSONObject(i);
			HAPChangeItem changeItem = HAPParserChange.parseChangeItem(changeJson);
			this.addInitialChange(changeItem);
		}
		
		JSONArray changeArray = jsonObj.getJSONArray(CHANGES);
		for(int i=0; i<changeArray.length(); i++) {
			JSONObject changeJson = changeArray.getJSONObject(i);
			HAPChangeItem changeItem = HAPParserChange.parseChangeItem(changeJson);
			this.addChange(changeItem);
		}
		
		JSONObject questionJson = jsonObj.getJSONObject(QUESTION);
		this.m_question = new  HAPQuestionGroup();
		this.m_question.buildObject(questionJson, HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INITIALCHANGE, HAPJsonUtility.buildJson(this.m_initialChange, HAPSerializationFormat.JSON));
		jsonMap.put(CHANGES, HAPJsonUtility.buildJson(this.m_changes, HAPSerializationFormat.JSON));
		jsonMap.put(QUESTION, HAPJsonUtility.buildJson(this.m_question, HAPSerializationFormat.JSON));
	}
}
