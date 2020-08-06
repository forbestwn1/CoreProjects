package com.nosliw.data.core.story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.design.HAPChangeItem;

public abstract class HAPStoryElementImp extends HAPEntityInfoImp implements HAPStoryElement{

	private String m_categary;
	
	private String m_type;
	
	private HAPStatus m_status;
	
	private boolean m_enable;

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
	public List<HAPChangeItem> patch(String path, Object value) {
		if(ENABLE.equals(path)) {
			this.m_enable = (Boolean)value;
			return new ArrayList<HAPChangeItem>();
		}
		return null;
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
