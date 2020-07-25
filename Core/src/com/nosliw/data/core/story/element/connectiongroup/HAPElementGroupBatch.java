package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStory;

public class HAPElementGroupBatch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstant.STORYGROUP_TYPE_BATCH; 

	public HAPElementGroupBatch(HAPStory story) {
		super(story);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public boolean patch(String path, Object value) {
		super.patch(path, value);
		for(HAPInfoElement ele : this.getElements()) {
			this.getStory().getElement(ele.getElementId()).patch(path, value);
		}
		return false;
	}
	
}
