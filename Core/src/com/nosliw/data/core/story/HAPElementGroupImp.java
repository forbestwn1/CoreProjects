package com.nosliw.data.core.story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPElementGroupImp extends HAPStoryElementImp implements HAPElementGroup{

	private List<HAPInfoElement> m_elements;

	private HAPStory m_story;
	
	public HAPElementGroupImp(HAPStory story) {
		super(HAPConstant.STORYELEMENT_CATEGARY_GROUP);  
		this.m_elements = new ArrayList<HAPInfoElement>();
		this.m_story = story;
	}
	
	public HAPElementGroupImp(String type, HAPStory story) {
		super(HAPConstant.STORYELEMENT_CATEGARY_GROUP, type);
		this.m_elements = new ArrayList<HAPInfoElement>();
		this.m_story = story;
	}

	@Override
	public List<HAPInfoElement> getElements() {  return this.m_elements;  }

	@Override
	public void addElement(HAPInfoElement eleId) {    
		eleId.setStory(this.m_story);
		this.m_elements.add(eleId);
	}
	
	protected HAPInfoElement getElement(String id) {
		for(HAPInfoElement ele : this.m_elements) {
			if(ele.getId().equals(id)) {
				return ele;
			}
		}
		return null;
	}
	
	protected HAPStory getStory() {    return this.m_story;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONArray eleArray = jsonObj.optJSONArray(ELEMENTS);
		if(eleArray!=null) {
			for(int i=0; i<eleArray.length(); i++) {
				HAPInfoElement eleInfo = new HAPInfoElement();
				eleInfo.buildObject(eleArray.get(i), HAPSerializationFormat.JSON);
				this.m_elements.add(eleInfo);
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTS, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}

}
