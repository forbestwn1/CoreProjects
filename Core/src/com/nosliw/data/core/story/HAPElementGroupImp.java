package com.nosliw.data.core.story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

public abstract class HAPElementGroupImp extends HAPStoryElementImp implements HAPElementGroup{

	private List<HAPInfoElement> m_elements;

	public HAPElementGroupImp() {
		super(HAPConstant.STORYELEMENT_CATEGARY_GROUP);  
		this.m_elements = new ArrayList<HAPInfoElement>();
	}
	
	public HAPElementGroupImp(String type) {
		super(HAPConstant.STORYELEMENT_CATEGARY_GROUP, type);
		this.m_elements = new ArrayList<HAPInfoElement>();
	}

	@Override
	public List<HAPInfoElement> getElements() {  return this.m_elements;  }

	@Override
	public HAPInfoElement addElement(HAPInfoElement eleId) {
		this.m_elements.add(eleId);
		if(this.getStory()!=null)  eleId.processAlias(this.getStory());
		return eleId;
	}
	
	@Override
	public void appendToStory(HAPStory story) {   
		super.appendToStory(story);
		for(HAPInfoElement ele : this.m_elements) {
			ele.processAlias(story);
		}
	}
	
	protected HAPInfoElement getElement(String id) {
		for(HAPInfoElement ele : this.m_elements) {
			if(ele.getId().equals(id)) {
				return ele;
			}
		}
		return null;
	}
	
	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out==null) {
			if(ELEMENT.equals(path)) {
				out = new HAPChangeResult();
				if(value instanceof HAPInfoElement) {
					//append
					HAPInfoElement added = this.addElement((HAPInfoElement)value);
					out.addRevertChange(HAPUtilityChange.buildChangePatch(this, ELEMENT, Integer.parseInt(added.getId())));
				}
				else if(value instanceof Integer) {
					//delete
					HAPInfoElement ele = null;
					for(int i=0; i<this.m_elements.size(); i++) {
						if(this.m_elements.get(i).getId().equals(value+"")) {
							ele = this.m_elements.get(i); 
							this.m_elements.remove(i);
							break;
						}
					}
					if(ele!=null)	out.addRevertChange(HAPUtilityChange.buildChangePatch(this, ELEMENT, ele));
				}
			}
		}
		return out;
	}

	protected void cloneTo(HAPElementGroupImp groupEle) {
		super.cloneTo(groupEle);
		for(HAPInfoElement ele : this.m_elements) {
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
