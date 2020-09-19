package com.nosliw.data.core.story.change;

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
public abstract class HAPChangeItem extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String CHANGETYPE = "changeType";

	@HAPAttribute
	public static final String REVERTCHANGES = "revertChanges";

	private String m_changeType;
	
	private List<HAPChangeItem> m_revertChanges;
	
	private HAPStory m_story;
	
	public HAPChangeItem(String changeType) {
		this.m_changeType = changeType;
	}

	public String getChangeType() {    return this.m_changeType;    }
	
	public void setRevertChanges(List<HAPChangeItem> revertChanges) {    this.m_revertChanges = revertChanges;      }
	public List<HAPChangeItem> getRevertChanges(){     return this.m_revertChanges;       }
	
	public void setStory(HAPStory story) {    this.m_story = story;    }
	protected HAPStory getStory() {    return this.m_story;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_changeType = jsonObj.getString(CHANGETYPE);
		
		JSONArray revertChangesArray = jsonObj.optJSONArray(REVERTCHANGES);
		if(revertChangesArray!=null) {
			this.m_revertChanges = new ArrayList<HAPChangeItem>();
			for(int i=0; i<revertChangesArray.length(); i++) {
				this.m_revertChanges.add(HAPParserChange.parseChangeItem(revertChangesArray.getJSONObject(i)));
			}
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHANGETYPE, this.m_changeType);
		if(this.m_revertChanges!=null)   jsonMap.put(REVERTCHANGES, HAPJsonUtility.buildJson(this.m_revertChanges, HAPSerializationFormat.JSON));
	}
}
