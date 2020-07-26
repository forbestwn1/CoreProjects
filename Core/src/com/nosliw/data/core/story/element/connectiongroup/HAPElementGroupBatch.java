package com.nosliw.data.core.story.element.connectiongroup;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPChangeItemPatch;

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
	public List<HAPChangeItem> patch(String path, Object value) {
		List<HAPChangeItem> out = super.patch(path, value);
		if(out==null)  return out;
		else {
			for(HAPInfoElement ele : this.getElements()) {
				out.add(new HAPChangeItemPatch(ele.getElementId().getCategary(), ele.getElementId().getId(), path, value));
			}
		}
		return out;
	}
	
}
