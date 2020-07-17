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
import com.nosliw.data.core.story.HAPParserStory;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryImp;

@HAPEntityWithAttribute
public class HAPDesignStory extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DIRECTORID = "directorId";

	@HAPAttribute
	public static final String STORY = "story";

	@HAPAttribute
	public static final String CHANGEHISTORY = "changeHistory";

	private static final String INFO_IDINDEX = "design_idindex";
	
	private String m_directorId;
	
	private HAPStory m_story;
	
	private List<HAPChangeBatch> m_changeHistory;
	
	public HAPDesignStory() {
		this.m_story = new HAPStoryImp();
		this.m_changeHistory = new ArrayList<HAPChangeBatch>();
	}
	
	public HAPDesignStory(String designId, String directorId) {
		this();
		this.setId(designId);
		this.m_directorId = directorId;
	}
	
	public String getDirectorId() {	return this.m_directorId;	}

	public HAPStory getStory() {	return this.m_story;	}
	
	public String getNextId() {
		Integer index = (Integer)this.getInfoValue(INFO_IDINDEX);
		if(index==null) {
			index = Integer.valueOf(0);
		}
		index++;
		this.getInfo().setValue(INFO_IDINDEX, index);
		return index + "";	
	}
	
	public void addChangeBatch(HAPChangeBatch changeBatch) {
		changeBatch.setStory(this.m_story);
		this.m_changeHistory.add(changeBatch);     
	}
	
	public List<HAPChangeBatch> getChangeHistory(){    return this.m_changeHistory;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_directorId = (String)jsonObj.opt(DIRECTORID);
		JSONObject storyJsonObj = jsonObj.getJSONObject(STORY);
		this.m_story = HAPParserStory.parseStory(storyJsonObj);
		
		JSONArray changeHistoryArray = jsonObj.optJSONArray(CHANGEHISTORY);
		for(int i=0; i<changeHistoryArray.length(); i++) {
			JSONObject changeHistoryItem = changeHistoryArray.getJSONObject(i);
			HAPChangeBatch changeBatch = new HAPChangeBatch();
			changeBatch.buildObject(changeHistoryItem, HAPSerializationFormat.JSON);
			this.addChangeBatch(changeBatch);
		}
		return true;  
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DIRECTORID, this.m_directorId);
		jsonMap.put(STORY, this.m_story.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(CHANGEHISTORY, HAPJsonUtility.buildJson(this.m_changeHistory, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DIRECTORID, this.m_directorId);
		jsonMap.put(STORY, this.m_story.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CHANGEHISTORY, HAPJsonUtility.buildJson(this.m_changeHistory, HAPSerializationFormat.JSON));
	}
}
