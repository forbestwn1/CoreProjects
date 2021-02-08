package com.nosliw.data.core.story.element.node;

import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

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
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(SCRIPT.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, SCRIPT, this.m_script));
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
