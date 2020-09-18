package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPParserElement;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPChangeItemNew extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstant.STORYDESIGN_CHANGETYPE_NEW;

	@HAPAttribute
	public static final String ALIAS = "alias";

	@HAPAttribute
	public static final String ELEMENT = "element";

	private HAPStoryElement m_element;
	
	private String m_alias;
	
	public HAPChangeItemNew() {}

	public HAPChangeItemNew(HAPStoryElement element, String alias) {
		this(element.getCategary(), element.getId(), alias);
		this.m_element = element;
	}

	public HAPChangeItemNew(String targetCategary, String targetId, String alias) {
		super(MYCHANGETYPE, targetCategary, targetId);
	}
	
	public String getAlias() {	return this.m_alias;	}
	
	public HAPStoryElement getElement() {
		HAPStoryElement out = this.m_element;
		if(out==null)   out = this.getStory().getElement(this.getTargetCategary(), this.getTargetId());
		return out;
	}
	public void setElement(HAPStoryElement element) {   this.m_element = element;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_alias = (String)jsonObj.opt(ALIAS);
		JSONObject eleObj = jsonObj.optJSONObject(ELEMENT);
		if(eleObj!=null) {
			HAPParserElement.parseElement(eleObj, this.getStory());
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, this.getElement().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, this.m_alias);
	}
}
