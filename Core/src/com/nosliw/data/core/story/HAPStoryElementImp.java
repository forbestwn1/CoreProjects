package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

public abstract class HAPStoryElementImp extends HAPEntityInfoImp implements HAPStoryElement{

	private String m_categary;
	
	private String m_type;
	
	private HAPStatus m_status;
	
	private boolean m_enable;

	private HAPStory m_story;
	
	public HAPStoryElementImp() {
		this.m_status = new HAPStatus();
		this.m_enable = true;
	}

	public HAPStoryElementImp(String categary) {
		this();
		this.m_categary = categary;
	}
	
	public HAPStoryElementImp(String categary, String type) {
		this(categary);
		this.m_type = type;
	}
	
	@Override
	public void appendToStory(HAPStory story) {
		this.m_story = story;     
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstant.ENTITY;    }

	@Override
	public HAPIdElement getElementId() {	return new HAPIdElement(this.m_categary, this.getId());	}

	@Override
	public String getCategary() {   return this.m_categary;    }

	@Override
	public String getType() {  return this.m_type; }

	@Override
	public HAPStatus getStatus() {  return this.m_status;  }

	@Override
	public boolean isEnable() {   return this.m_enable;   }

	@Override
	public Object getValueByPath(String path) {
		Object out = null;
		if(ENABLE.equals(path)) {
			return this.m_enable;
		}
		return out;
	}
	
	@Override
	public HAPChangeResult patch(String path, Object value) {
		String[] pathSegs = HAPNamingConversionUtility.parsePaths(path);
		HAPChangeResult out = new HAPChangeResult(HAPStoryElement.class);
		if(ENABLE.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.m_enable));
			this.m_enable = (Boolean)value;
			return out;
		}
		else if(NAME.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.getName()));
			this.setName((String)value);
			return out;
		}
		else if(DISPLAYNAME.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.getName()));
			this.setDisplayName((String)value);
			return out;
		}
		else if(DESCRIPTION.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.getDescription()));
			this.setDescription((String)value);
			return out;
		}
		else if(pathSegs.length>1) {
			if(pathSegs[0].equals(HAPEntityInfo.INFO)) {
				Object oldValue = this.getInfo().setValue(pathSegs[1], value);
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, oldValue));
				return out;
			}
		}
		return null;
	}

	protected HAPStory getStory() {    return this.m_story;    }

	protected void cloneTo(HAPStoryElementImp storyEle) {
		this.cloneToEntityInfo(storyEle);
		storyEle.m_story = this.m_story;
		storyEle.m_categary = this.m_categary;
		storyEle.m_type = this.m_type;
		storyEle.m_status = this.m_status.cloneStatus();
		storyEle.m_enable = this.m_enable;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		Object categaryObj = jsonObj.opt(CATEGARY);
		if(categaryObj!=null)  this.m_categary = (String)categaryObj;
		this.m_type = jsonObj.getString(TYPE);
		this.m_status.buildObject(jsonObj.optJSONObject(STATUS), HAPSerializationFormat.JSON);
		Object enableValue = jsonObj.opt(ENABLE);
		if(enableValue!=null)	this.m_enable = (Boolean)enableValue; 
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(STATUS, HAPJsonUtility.buildJson(this.m_status, HAPSerializationFormat.JSON));
		jsonMap.put(ENABLE, this.m_enable+"");
		typeJsonMap.put(ENABLE, Boolean.class);
	}
}
