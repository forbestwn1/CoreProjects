package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPParserElement;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPChangeItemNew extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW;

	@HAPAttribute
	public static final String ALIAS = "alias";

	@HAPAttribute
	public static final String ELEMENT = "element";

	private HAPAliasElement m_alias;
	
	private HAPStoryElement m_storyElement;
	
	public HAPChangeItemNew() {
		super(MYCHANGETYPE);
	}

	public HAPChangeItemNew(HAPStoryElement storyElement, HAPAliasElement alias) {
		this();
		this.m_alias = alias;
		this.m_storyElement = storyElement;
	}
	
	public HAPChangeItemNew(HAPStoryElement storyElement) {
		this(storyElement, null);
	}

	public HAPAliasElement getAlias() {	return this.m_alias;	}

	public HAPStoryElement getElement() {  return this.m_storyElement;  }
	
	public void setElement(HAPStoryElement storyEle) {    this.m_storyElement = storyEle;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONObject aliasObj = jsonObj.optJSONObject(ALIAS);
		if(aliasObj!=null) {
			this.m_alias = new HAPAliasElement();
			this.m_alias.buildObject(aliasObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject eleObj = jsonObj.optJSONObject(ELEMENT);
		if(eleObj!=null) {
			this.m_storyElement = HAPParserElement.parseElement(eleObj);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, this.getElement().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ALIAS, HAPJsonUtility.buildJson(this.m_alias, HAPSerializationFormat.JSON));
	}
}
