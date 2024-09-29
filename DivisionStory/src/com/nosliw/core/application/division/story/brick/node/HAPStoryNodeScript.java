package com.nosliw.core.application.division.story.brick.node;

import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNodeImp;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryNodeScript extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SCRIPT; 
	
	@HAPAttribute
	public static final String SCRIPT = "script";
	
	private String m_script;
	
	public HAPStoryNodeScript() {
		super(STORYNODE_TYPE);
	}
	
	public HAPStoryNodeScript(String script) {
		this();
		this.setScript(script);
	}
	
	public String getScript() {
		return StringEscapeUtils.unescapeEcmaScript(this.m_script);
//		return this.m_script;
	}
	public void setScript(String script) {	
		this.m_script = StringEscapeUtils.escapeEcmaScript(script);
//		this.m_script = script;
	}

	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPStoryChangeResult();
			if(SCRIPT.equals(path)) {
				out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, SCRIPT, this.m_script));
				this.m_script = (String)value;
				return out;
			}
		}
		return null;
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeScript out = new HAPStoryNodeScript();
		super.cloneTo(out);
		out.m_script = this.m_script;
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.setScript(jsonObj.getString(SCRIPT));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, this.m_script);
	}
}
