package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.change.HAPChangeItemPatch;
import com.nosliw.data.core.story.change.HAPChangeResult;

public class HAPElementGroupBatch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstant.STORYGROUP_TYPE_BATCH; 

	public HAPElementGroupBatch() {
		super(GROUP_TYPE);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPElementGroupBatch out = new HAPElementGroupBatch();
		super.cloneTo(out);
		return out;
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
	public HAPChangeResult patch(String path, Object value) {
		HAPChangeResult out = super.patch(path, value);
		if(out==null)  return out;
		else {
			for(HAPInfoElement ele : this.getElements()) {
				out.addExtraChange(new HAPChangeItemPatch(ele.getElementId(), path, value));
			}
		}
		return out;
	}
}
