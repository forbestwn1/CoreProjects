package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryStory;

public class HAPStoryQuestionGroup extends HAPStoryQuestion{

	@HAPAttribute
	public static final String CHILDREN = "children";

	private List<HAPStoryQuestion> m_children;

	public HAPStoryQuestionGroup() {
		this.m_children = new ArrayList<HAPStoryQuestion>();
	}
	
	public HAPStoryQuestionGroup(String question) {
		super(question);
		this.m_children = new ArrayList<HAPStoryQuestion>();
	}

	@Override
	public String getType() {	return HAPConstantShared.STORYDESIGN_QUESTIONTYPE_GROUP;	}

	@Override
	public void setId(String id) {
		for(int i=0; i<this.m_children.size(); i++) {
			this.m_children.get(i).setId(id+"_"+i);
		}
	}

	@Override
	public void processAlias(HAPStoryStory story) {
		for(HAPStoryQuestion child : this.m_children)  child.processAlias(story);
	}

	public List<HAPStoryQuestion> getChildren(){   return this.m_children;   }
	public void addChild(HAPStoryQuestion item) {	this.m_children.add(item);	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONArray itemArray = jsonObj.optJSONArray(CHILDREN);
		if(itemArray!=null) {
			for(int i=0; i<itemArray.length(); i++) {
				HAPStoryQuestion question = HAPStoryQuestion.parseQuestion(itemArray.getJSONObject(i));
				this.addChild(question);
			}
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHILDREN, HAPUtilityJson.buildJson(this.m_children, HAPSerializationFormat.JSON));
	}
}
