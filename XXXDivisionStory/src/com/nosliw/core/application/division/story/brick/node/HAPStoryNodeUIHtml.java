package com.nosliw.core.application.division.story.brick.node;

import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPStoryNodeUIHtml extends HAPStoryNodeUI{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_HTML; 
	
	@HAPAttribute
	public static final String HTML = "html";

	private String m_html;
	
	public HAPStoryNodeUIHtml() {	super(STORYNODE_TYPE);	}
	
	public HAPStoryNodeUIHtml(String html) {
		this();
		this.m_html = html;
	}
	
	public String getHtml() {   return this.m_html;     }
	public void setHtml(String html) {    this.m_html = html;     }
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null) {
			return out;
		} else {
			out = new HAPStoryChangeResult();
			if(HTML.equals(path)) {
				out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, HTML, this.m_html));
				this.m_html = (String)value;
				return out;
			}
		}
		return null;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_html = StringEscapeUtils.unescapeJson(jsonObj.optString(HTML));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HTML, StringEscapeUtils.escapeJson(this.m_html));
	}
}
