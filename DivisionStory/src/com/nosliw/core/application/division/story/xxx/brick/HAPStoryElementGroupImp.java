package com.nosliw.core.application.division.story.xxx.brick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPStoryElementGroupImp extends HAPStoryElementImp implements HAPStoryElementGroup{

	private List<HAPStoryInfoElement> m_elements;

	public HAPStoryElementGroupImp() {
		super(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP);  
		this.m_elements = new ArrayList<HAPStoryInfoElement>();
	}
	
	public HAPStoryElementGroupImp(String type) {
		super(HAPConstantShared.STORYELEMENT_CATEGARY_GROUP, type);
		this.m_elements = new ArrayList<HAPStoryInfoElement>();
	}

	@Override
	public List<HAPStoryInfoElement> getElements() {  return this.m_elements;  }

	@Override
	public HAPStoryInfoElement addElement(HAPStoryInfoElement eleId) {
		this.m_elements.add(eleId);
		if(this.getStory()!=null)  eleId.processAlias(this.getStory());
		return eleId;
	}
	
	@Override
	public void appendToStory(HAPStoryStory story) {   
		super.appendToStory(story);
		for(HAPStoryInfoElement ele : this.m_elements) {
			ele.processAlias(story);
		}
	}
	
	protected HAPStoryInfoElement getElement(String id) {
		for(HAPStoryInfoElement ele : this.m_elements) {
			if(ele.getId().equals(id)) {
				return ele;
			}
		}
		return null;
	}
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out==null) {
			if(ELEMENT.equals(path)) {
				out = new HAPStoryChangeResult();
				if(value instanceof HAPStoryInfoElement) {
					//append
					HAPStoryInfoElement added = this.addElement((HAPStoryInfoElement)value);
					out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, ELEMENT, Integer.parseInt(added.getId())));
				}
				else if(value instanceof Integer) {
					//delete
					HAPStoryInfoElement ele = null;
					for(int i=0; i<this.m_elements.size(); i++) {
						if(this.m_elements.get(i).getId().equals(value+"")) {
							ele = this.m_elements.get(i); 
							this.m_elements.remove(i);
							break;
						}
					}
					if(ele!=null)	out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, ELEMENT, ele));
				}
			}
		}
		return out;
	}

	protected void cloneTo(HAPStoryElementGroupImp groupEle) {
		super.cloneTo(groupEle);
		for(HAPStoryInfoElement ele : this.m_elements) {
			groupEle.m_elements.add(ele.cloneElementInfo());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONArray eleArray = jsonObj.optJSONArray(ELEMENTS);
		if(eleArray!=null) {
			for(int i=0; i<eleArray.length(); i++) {
				HAPStoryInfoElement eleInfo = new HAPStoryInfoElement();
				eleInfo.buildObject(eleArray.get(i), HAPSerializationFormat.JSON);
				this.m_elements.add(eleInfo);
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTS, HAPUtilityJson.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}
}
