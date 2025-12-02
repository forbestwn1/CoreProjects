package com.nosliw.core.application.division.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPStoryAliasElement extends HAPSerializableImp implements HAPStoryReferenceElement{
	
	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String TEMPORARY = "temporary";

	private String m_name;

	private boolean m_isTemp;
	
	public HAPStoryAliasElement() {}

	public HAPStoryAliasElement(String alias) {
		this(alias, true);
	}

	public HAPStoryAliasElement(String alias, boolean isTemp) {
		this.m_name = alias;
		this.m_isTemp = isTemp;
	}

	public String getName() {		return this.m_name;	}
	public boolean isTemporary() {   return this.m_isTemp;    }

	@Override
	public String getEntityOrReferenceType() {  return HAPConstantShared.REFERENCE;  }

	@Override
	public HAPStoryReferenceElement cloneElementReference() {
		HAPStoryAliasElement out = new HAPStoryAliasElement();
		out.m_isTemp = this.m_isTemp;
		out.m_name = this.m_name;
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Object nameObj = jsonObj.opt(NAME);
		if(nameObj!=null)  this.m_name = (String)nameObj;
		Object tempObj = jsonObj.opt(TEMPORARY);
		if(tempObj!=null)  this.m_isTemp = (Boolean)tempObj;
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(TEMPORARY, this.m_isTemp+"");
		typeJsonMap.put(TEMPORARY, Boolean.class);
	}
}
