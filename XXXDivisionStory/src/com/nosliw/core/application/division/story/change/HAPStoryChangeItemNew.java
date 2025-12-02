package com.nosliw.core.application.division.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryParserElement;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;

public class HAPStoryChangeItemNew extends HAPStoryChangeItem{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW;

	@HAPAttribute
	public static final String ALIAS = "alias";

	@HAPAttribute
	public static final String ELEMENT = "element";

	private HAPStoryAliasElement m_alias;
	
	private HAPStoryElement m_storyElement;
	
	public HAPStoryChangeItemNew() {
		super(MYCHANGETYPE);
	}

	public HAPStoryChangeItemNew(HAPStoryElement storyElement, HAPStoryAliasElement alias) {
		this();
		this.m_alias = alias;
		this.m_storyElement = storyElement;
	}
	
	public HAPStoryChangeItemNew(HAPStoryElement storyElement) {
		this(storyElement, null);
	}

	public HAPStoryAliasElement getAlias() {	return this.m_alias;	}

	public HAPStoryElement getElement() {  return this.m_storyElement;  }
	
	public void setElement(HAPStoryElement storyEle) {    this.m_storyElement = storyEle;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONObject aliasObj = jsonObj.optJSONObject(ALIAS);
		if(aliasObj!=null) {
			this.m_alias = new HAPStoryAliasElement();
			this.m_alias.buildObject(aliasObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject eleObj = jsonObj.optJSONObject(ELEMENT);
		if(eleObj!=null) {
			this.m_storyElement = HAPStoryParserElement.parseElement(eleObj);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, this.getElement().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPUtilityJson.buildJson(this.m_alias, HAPSerializationFormat.JSON));
	}
}
