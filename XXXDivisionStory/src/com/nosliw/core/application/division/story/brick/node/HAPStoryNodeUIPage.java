package com.nosliw.core.application.division.story.brick.node;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPStoryNodeUIPage extends HAPStoryNodeUI{
	
	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_PAGE; 
	
	public HAPStoryNodeUIPage() {
		super(STORYNODE_TYPE);
	}
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeUIPage out = new HAPStoryNodeUIPage();
		super.cloneToUIStoryNode(out);
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
}
