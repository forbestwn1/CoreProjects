package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

public abstract class HAPStoryElementImp extends HAPEntityInfoImp implements HAPStoryElement{

	private String m_categary;
	
	private String m_type;
	
	private HAPStatus m_status;
	
	private boolean m_enable;

	private Object m_extra;
	
	private HAPDisplayResourceNode m_displayResource;
	
	private HAPStory m_story;
	
	public HAPStoryElementImp() {
		this.m_status = new HAPStatus();
		this.m_enable = true;
		this.m_displayResource = new HAPDisplayResourceNode();
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
	public String getDisplayName() {
		String out = null;
		if(this.m_displayResource!=null)   out = this.m_displayResource.getValue(DISPLAYNAME);
		if(out==null)  out = super.getDisplayName();
		return out;
	}
	
	@Override
	public void appendToStory(HAPStory story) {
		this.m_story = story;     
	}

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
	public HAPIdElement getElementId() {	return new HAPIdElement(this.m_categary, this.getId());	}

	@Override
	public String getCategary() {   return this.m_categary;    }

	@Override
	public String getType() {  return this.m_type; }

	@Override
	public HAPStatus getElementStatus() {  return this.m_status;  }

	@Override
	public boolean isEnable() {   return this.m_enable;   }

	@Override
	public Object getExtra() {  return this.m_extra;	}

	public void setExtra(Object extra) {   this.m_extra = extra;    }
	
	@Override
	public HAPDisplayResourceNode getDisplayResource() {    return this.m_displayResource;     }
	public void setDisplayResource(Object obj) {
		if(obj==null)   this.m_displayResource = null;
		else if(obj instanceof HAPDisplayResourceNode)  this.m_displayResource = (HAPDisplayResourceNode)obj;
		else if(obj instanceof JSONObject) {
			this.m_displayResource = new HAPDisplayResourceNode();
			this.m_displayResource.buildObject(obj, HAPSerializationFormat.JSON);
		}
	}
	
	@Override
	public Object getValueByPath(String path) {
		Object out = null;
		if(ENABLE.equals(path)) {
			return this.m_enable;
		}
		return out;
	}
	
	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
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
		else if(DISPLAYRESOURCE.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.getDisplayResource()));
			this.setDisplayResource(value);
			return out;
		}
		else if(EXTRA.equals(path)) {
			out.addRevertChange(HAPUtilityChange.buildChangePatch(this, path, this.getExtra()));
			this.setExtra(value);
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
		storyEle.m_extra = this.m_extra;
		storyEle.m_displayResource = (HAPDisplayResourceNode)this.m_displayResource.cloneDisplayResource(); 
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		Object categaryObj = jsonObj.opt(CATEGARY);
		if(categaryObj!=null)  this.m_categary = (String)categaryObj;
		this.m_type = jsonObj.getString(TYPE);
		this.m_status.buildObject(jsonObj.optJSONObject(ELEMENTSTATUS), HAPSerializationFormat.JSON);
		Object enableValue = jsonObj.opt(ENABLE);
		if(enableValue!=null)	this.m_enable = (Boolean)enableValue;
		this.m_extra = jsonObj.opt(EXTRA);
		JSONObject displayResourceObj = jsonObj.optJSONObject(DISPLAYRESOURCE);
		if(displayResourceObj!=null)   this.m_displayResource.buildObject(displayResourceObj, HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(ELEMENTSTATUS, HAPJsonUtility.buildJson(this.m_status, HAPSerializationFormat.JSON));
		jsonMap.put(ENABLE, this.m_enable+"");
		typeJsonMap.put(ENABLE, Boolean.class);
		jsonMap.put(EXTRA, HAPJsonUtility.buildJson(this.m_extra, HAPSerializationFormat.JSON));
		jsonMap.put(DISPLAYRESOURCE, HAPJsonUtility.buildJson(this.m_displayResource, HAPSerializationFormat.JSON));
	}
}
