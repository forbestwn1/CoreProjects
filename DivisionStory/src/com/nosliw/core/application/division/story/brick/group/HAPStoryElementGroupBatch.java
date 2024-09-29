package com.nosliw.core.application.division.story.brick.group;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroup;
import com.nosliw.core.application.division.story.brick.HAPStoryElementGroupImp;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItemPatch;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryElementGroupBatch extends HAPStoryElementGroupImp{

	public final static String GROUP_TYPE = HAPConstantShared.STORYGROUP_TYPE_BATCH; 

	public HAPStoryElementGroupBatch() {
		super(GROUP_TYPE);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryElementGroupBatch out = new HAPStoryElementGroupBatch();
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
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out==null)  return out;
		else {
			if(HAPStoryElement.class.equals(out.getProcessor())) {
				//only for attribute defined in story element, apply to children 
				for(HAPStoryInfoElement ele : this.getElements()) {
					out.addExtendChange(new HAPStoryChangeItemPatch(ele.getElementId(), path, value));
				}
			}
			else if(HAPStoryElementGroup.ELEMENT.equals(path)) {
				if(value instanceof HAPStoryInfoElement) {
					//for new element
					HAPStoryInfoElement eleInfo = (HAPStoryInfoElement)value;
					for(String rootAttr : HAPStoryElement.getRootAttribute()) {
						out.addExtendChange(new HAPStoryChangeItemPatch(eleInfo.getElementId(), rootAttr, this.getValueByPath(rootAttr)));
					}
				}
			}
		}
		return out;
	}
}
