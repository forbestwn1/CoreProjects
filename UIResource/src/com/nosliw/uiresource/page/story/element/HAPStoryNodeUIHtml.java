package com.nosliw.uiresource.page.story.element;

import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.change.HAPChangeResult;
import com.nosliw.data.core.story.change.HAPUtilityChange;

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
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null)  return out; 
		else {
			out = new HAPChangeResult();
			if(HTML.equals(path)) {
				out.addRevertChange(HAPUtilityChange.buildChangePatch(this, HTML, this.m_html));
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
