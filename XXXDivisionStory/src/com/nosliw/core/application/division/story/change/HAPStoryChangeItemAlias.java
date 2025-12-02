package com.nosliw.core.application.division.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryIdElement;

public class HAPStoryChangeItemAlias extends HAPStoryChangeItem{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_ALIAS;

	@HAPAttribute
	public static final String ALIAS = "alias";

	@HAPAttribute
	public static final String ELEMENTID = "elementId";
	
	private HAPStoryAliasElement m_alias;
	
	private HAPStoryIdElement m_eleId;
	
	public HAPStoryChangeItemAlias() {
		super(MYCHANGETYPE);
	}
	
	public HAPStoryChangeItemAlias(HAPStoryAliasElement alias, HAPStoryIdElement eleId) {
		this();
		this.m_alias = alias;
		this.m_eleId = eleId;
	}
	
	public HAPStoryAliasElement getAlias() {   return this.m_alias;    }
	public HAPStoryIdElement getElementId() {   return this.m_eleId;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONObject aliasObj = jsonObj.getJSONObject(ALIAS);
		if(aliasObj!=null) {
			this.m_alias = new HAPStoryAliasElement();
			this.m_alias.buildObject(aliasObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject eleIdObj = jsonObj.getJSONObject(ELEMENTID);
		if(eleIdObj!=null) {
			this.m_eleId = new HAPStoryIdElement();
			this.m_eleId.buildObject(eleIdObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ALIAS, HAPUtilityJson.buildJson(this.m_alias, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTID, HAPUtilityJson.buildJson(this.m_eleId, HAPSerializationFormat.JSON));
	}
}
