package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryParserChange;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryAnswer extends HAPSerializableImp{

	@HAPAttribute
	public static final String CHANGES = "changes";

	@HAPAttribute
	public static final String QUESTIONID = "questionId";
	
	private List<HAPStoryChangeItem> m_changes;
	
	private String m_questionId;
	
	public HAPStoryAnswer() {
		this.m_changes = new ArrayList<HAPStoryChangeItem>();
	}
	
	public List<HAPStoryChangeItem> getChanges(){    return this.m_changes;    }
	public void setChanges(List<HAPStoryChangeItem> changes) {  
		this.m_changes.clear();
		this.m_changes.addAll(changes);
	}
	
	public void addChange(HAPStoryChangeItem change) {		this.m_changes.add(change);	}

	public String getQuestionId() {   return this.m_questionId;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONArray changesArray = jsonObj.optJSONArray(CHANGES);
		if(changesArray!=null) {
			for(int i=0; i<changesArray.length(); i++) {
				this.m_changes.add(HAPStoryParserChange.parseChangeItem(changesArray.getJSONObject(i)));
			}
		}
		this.m_questionId = (String)jsonObj.opt(QUESTIONID);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(QUESTIONID, this.m_questionId);
		jsonMap.put(CHANGES, HAPUtilityJson.buildJson(this.m_changes, HAPSerializationFormat.JSON));
	}
}
