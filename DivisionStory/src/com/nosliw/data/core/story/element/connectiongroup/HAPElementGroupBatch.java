package com.nosliw.data.core.story.element.connectiongroup;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPElementGroup;
import com.nosliw.data.core.story.HAPElementGroupImp;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.change.HAPChangeItemPatch;
import com.nosliw.data.core.story.change.HAPChangeResult;

public class HAPElementGroupBatch extends HAPElementGroupImp{

	public final static String GROUP_TYPE = HAPConstantShared.STORYGROUP_TYPE_BATCH; 

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
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out==null)  return out;
		else {
			if(HAPStoryElement.class.equals(out.getProcessor())) {
				//only for attribute defined in story element, apply to children 
				for(HAPInfoElement ele : this.getElements()) {
					out.addExtendChange(new HAPChangeItemPatch(ele.getElementId(), path, value));
				}
			}
			else if(HAPElementGroup.ELEMENT.equals(path)) {
				if(value instanceof HAPInfoElement) {
					//for new element
					HAPInfoElement eleInfo = (HAPInfoElement)value;
					for(String rootAttr : HAPStoryElement.getRootAttribute()) {
						out.addExtendChange(new HAPChangeItemPatch(eleInfo.getElementId(), rootAttr, this.getValueByPath(rootAttr)));
					}
				}
			}
		}
		return out;
	}
}
