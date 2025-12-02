package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public abstract class HAPStoryChangeItem extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String CHANGETYPE = "changeType";

	@HAPAttribute
	public static final String REVERTCHANGES = "revertChanges";

	@HAPAttribute
	public static final String REVERTABLE = "revertable";

	@HAPAttribute
	public static final String EXTENDFROM = "extendFrom";

	@HAPAttribute
	public static final String EXTENDED = "extended";

	private String m_changeType;
	
	private List<HAPStoryChangeItem> m_revertChanges;

	private boolean m_revertable;

	private String m_extendedFrom;
	
	private boolean m_extended;
	
	public HAPStoryChangeItem(String changeType) {
		this.m_changeType = changeType;
		this.m_revertable = true;
		this.m_extended = false;
	}

	public String getChangeType() {    return this.m_changeType;    }
	
	public void setRevertChanges(List<HAPStoryChangeItem> revertChanges) {    this.m_revertChanges = revertChanges;      }
	public List<HAPStoryChangeItem> getRevertChanges(){     return this.m_revertChanges;       }
	
	public boolean isRevertable() {    return this.m_revertable;     }
	public void setRevertable(boolean revertable) {     this.m_revertable = revertable;      }
	
	public String getExtendFrom() {    return this.m_extendedFrom;      }
	public void setExtendFrom(String extendFrom) {     this.m_extendedFrom = extendFrom;       }
	
	public boolean isExtended() {   return this.m_extended;     }
	public void setExtended() {    this.m_extended = true;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_changeType = jsonObj.getString(CHANGETYPE);
		Object revertableObj = jsonObj.opt(REVERTABLE);
		if(revertableObj!=null)  this.m_revertable = (Boolean)revertableObj; 
		this.m_extendedFrom = (String)jsonObj.opt(EXTENDFROM);
		if(jsonObj.opt(EXTENDED)!=null) this.m_extended = jsonObj.getBoolean(EXTENDED);
		
		JSONArray revertChangesArray = jsonObj.optJSONArray(REVERTCHANGES);
		if(revertChangesArray!=null) {
			this.m_revertChanges = new ArrayList<HAPStoryChangeItem>();
			for(int i=0; i<revertChangesArray.length(); i++) {
				this.m_revertChanges.add(HAPStoryParserChange.parseChangeItem(revertChangesArray.getJSONObject(i)));
			}
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHANGETYPE, this.m_changeType);
		if(this.m_revertChanges!=null)   jsonMap.put(REVERTCHANGES, HAPUtilityJson.buildJson(this.m_revertChanges, HAPSerializationFormat.JSON));
		jsonMap.put(REVERTABLE, this.m_revertable+"");
		typeJsonMap.put(REVERTABLE, Boolean.class);
		jsonMap.put(EXTENDFROM, this.m_extendedFrom);
		jsonMap.put(EXTENDED, this.m_extended+"");
		typeJsonMap.put(EXTENDED, Boolean.class);
	}
}
