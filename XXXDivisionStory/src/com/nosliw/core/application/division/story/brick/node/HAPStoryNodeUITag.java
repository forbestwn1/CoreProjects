package com.nosliw.core.application.division.story.brick.node;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public abstract class HAPStoryNodeUITag extends HAPStoryNodeUI{

	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	
	private String m_tagName;
	
	private Map<String, String> m_attributes;
	
	public HAPStoryNodeUITag() {
		this.m_attributes = new LinkedHashMap<String, String>();
	}
	
	public HAPStoryNodeUITag(String nodeType, String tagName, String id) {
		super(nodeType);
		this.m_tagName = tagName;
		this.setId(id);
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_attributes.put(HAPConstantShared.UIRESOURCE_ATTRIBUTE_STATICID, this.getId());
	}
	
	public String getTagName() {     return this.m_tagName;     }
	
	public void addAttribute(String name, String value) {     this.m_attributes.put(name, value);      }
	public Map<String, String> getAttributes(){   return this.m_attributes;     }
	public String getAttributeValue(String name) {  return this.m_attributes.get(name);   }
	
	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		HAPStoryChangeResult out = super.patch(path, value, runtimeEnv);
		if(out!=null) {
			return out;
		} else {
			out = new HAPStoryChangeResult();
			if(TAGNAME.equals(path)) {
				out.addRevertChange(HAPStoryUtilityChange.buildChangePatch(this, TAGNAME, this.m_tagName));
				this.m_tagName = (String)value;
				return out;
			}
		}
		return null;
	}

	protected void cloneToUITagStoryNode(HAPStoryNodeUITag node) {
		this.cloneToUIStoryNode(node);
		node.m_tagName = this.m_tagName;
		node.m_attributes.putAll(this.m_attributes);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_tagName = (String)jsonObj.opt(TAGNAME);
		
		JSONObject attrMap = jsonObj.optJSONObject(ATTRIBUTES);
		if(attrMap!=null) {
			for(Object key : attrMap.keySet()) {
				String name = (String)key;
				this.m_attributes.put(name, attrMap.getString(name));
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, m_tagName);
		jsonMap.put(ATTRIBUTES, HAPUtilityJson.buildMapJson(m_attributes));
	}

}
