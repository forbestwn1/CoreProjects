package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPIdElement;

public class HAPChangeItemAlias extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_ALIAS;

	@HAPAttribute
	public static final String ALIAS = "alias";

	@HAPAttribute
	public static final String ELEMENTID = "elementId";
	
	private HAPAliasElement m_alias;
	
	private HAPIdElement m_eleId;
	
	public HAPChangeItemAlias() {
		super(MYCHANGETYPE);
	}
	
	public HAPChangeItemAlias(HAPAliasElement alias, HAPIdElement eleId) {
		this();
		this.m_alias = alias;
		this.m_eleId = eleId;
	}
	
	public HAPAliasElement getAlias() {   return this.m_alias;    }
	public HAPIdElement getElementId() {   return this.m_eleId;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		JSONObject aliasObj = jsonObj.getJSONObject(ALIAS);
		if(aliasObj!=null) {
			this.m_alias = new HAPAliasElement();
			this.m_alias.buildObject(aliasObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject eleIdObj = jsonObj.getJSONObject(ELEMENTID);
		if(eleIdObj!=null) {
			this.m_eleId = new HAPIdElement();
			this.m_eleId.buildObject(eleIdObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ALIAS, HAPJsonUtility.buildJson(this.m_alias, HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTID, HAPJsonUtility.buildJson(this.m_eleId, HAPSerializationFormat.JSON));
	}
}
