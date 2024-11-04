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
import com.nosliw.core.application.division.story.HAPStoryParserStory;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryStoryImp;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryDesignStory extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DIRECTORID = "directorId";

	@HAPAttribute
	public static final String STORY = "story";

	@HAPAttribute
	public static final String CHANGEHISTORY = "changeHistory";

	private static final String INFO_IDINDEX = "design_idindex";
	
	private HAPStoryManagerChange m_changeMan;
	
	private String m_directorId;
	
	private HAPStoryStory m_story;
	
	private List<HAPStoryDesignStep> m_changeHistory;
	
	public HAPStoryDesignStory(HAPStoryManagerChange changeMan) {
		this.m_changeMan = changeMan;
		this.m_story = new HAPStoryStoryImp(changeMan);
		this.m_changeHistory = new ArrayList<HAPStoryDesignStep>();
	}
	
	public HAPStoryDesignStory(String designId, String directorId, HAPStoryManagerChange changeMan) {
		this(changeMan);
		this.setId(designId);
		this.m_directorId = directorId;
	}
	
	public HAPStoryDesignStep newStep() {	return new HAPStoryDesignStep(this.m_story);	}
	
	public String getDirectorId() {	return this.m_directorId;	}

	public HAPStoryStory getStory() {	return this.m_story;	}
	
	public String getNextId() {
		Integer index = (Integer)this.getInfoValue(INFO_IDINDEX);
		if(index==null) {
			index = Integer.valueOf(0);
		}
		index++;
		this.getInfo().setValue(INFO_IDINDEX, index);
		return index + "";	
	}
	
	public void addStep(HAPStoryDesignStep step) {
		this.m_changeHistory.add(step);     
	}
	
	public List<HAPStoryDesignStep> getChangeHistory(){    return this.m_changeHistory;    }
	public HAPStoryDesignStep getLatestStep() {   return this.m_changeHistory.get(this.m_changeHistory.size()-1);     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_directorId = (String)jsonObj.opt(DIRECTORID);
		JSONObject storyJsonObj = jsonObj.getJSONObject(STORY);
		this.m_story = HAPStoryParserStory.parseStory(storyJsonObj, this.m_changeMan);
		
		JSONArray changeHistoryArray = jsonObj.optJSONArray(CHANGEHISTORY);
		for(int i=0; i<changeHistoryArray.length(); i++) {
			JSONObject changeHistoryItem = changeHistoryArray.getJSONObject(i);
			HAPStoryDesignStep changeBatch = new HAPStoryDesignStep(this.m_story);
			changeBatch.buildObject(changeHistoryItem, HAPSerializationFormat.JSON);
			this.addStep(changeBatch);
		}
		return true;  
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DIRECTORID, this.m_directorId);
		jsonMap.put(STORY, this.m_story.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(CHANGEHISTORY, HAPUtilityJson.buildJson(this.m_changeHistory, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DIRECTORID, this.m_directorId);
		jsonMap.put(STORY, this.m_story.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CHANGEHISTORY, HAPUtilityJson.buildJson(this.m_changeHistory, HAPSerializationFormat.JSON));
	}
}
