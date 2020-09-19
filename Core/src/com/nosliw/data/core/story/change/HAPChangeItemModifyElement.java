package com.nosliw.data.core.story.change;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPReferenceElement;

public class HAPChangeItemModifyElement extends HAPChangeItem{

	@HAPAttribute
	public static final String TARGETELEMENTID = "targetElementId";

	private HAPIdElement m_targetElementId;
	
	private HAPAliasElement m_targetElementAlias;

	public HAPChangeItemModifyElement(String type) {
		super(type);
	}
	
	public HAPChangeItemModifyElement(String type, HAPReferenceElement targetElementRef) {
		this(type);
		if(targetElementRef instanceof HAPIdElement)   this.m_targetElementId = (HAPIdElement)targetElementRef;
		else if(targetElementRef instanceof HAPAliasElement)   this.m_targetElementAlias = (HAPAliasElement)targetElementRef;
	}
	
	public HAPIdElement getTargetElementId() {
		if(this.m_targetElementId==null) {
			this.m_targetElementId = this.getStory().getElementId(this.m_targetElementAlias.getAlias());
		}
		return this.m_targetElementId;   
	}
	
	public String getTargetCategary() {   return this.getTargetElementId().getCategary();   }
	
	public String getTargetId() {   return this.getTargetElementId().getId();    }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_targetElementId = new HAPIdElement();
		this.m_targetElementId.buildObject(jsonObj.getJSONObject(TARGETELEMENTID), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETELEMENTID, HAPJsonUtility.buildJson(this.m_targetElementId, HAPSerializationFormat.JSON));
	}
}
