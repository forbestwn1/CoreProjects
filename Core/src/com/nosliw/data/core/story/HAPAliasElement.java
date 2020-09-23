package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPAliasElement extends HAPSerializableImp implements HAPReferenceElement{
	
	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String TEMPORARY = "temporary";

	private String m_name;

	private boolean m_isTemp;
	
	public HAPAliasElement() {}

	public HAPAliasElement(String alias) {
		this(alias, true);
	}

	public HAPAliasElement(String alias, boolean isTemp) {
		this.m_name = alias;
		this.m_isTemp = isTemp;
	}

	public String getName() {		return this.m_name;	}
	public boolean isTemporary() {   return this.m_isTemp;    }

	@Override
	public String getEntityOrReferenceType() {  return HAPConstant.REFERENCE;  }

	@Override
	public HAPReferenceElement cloneElementReference() {
		HAPAliasElement out = new HAPAliasElement();
		out.m_isTemp = this.m_isTemp;
		out.m_name = this.m_name;
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_isTemp = (Boolean)jsonObj.opt(TEMPORARY);
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
