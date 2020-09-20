package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoElement extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ELEMENTID = "elementId";

	private HAPReferenceElement m_eleRef;
	
	private HAPStory m_story;
	
	public HAPInfoElement() {}
	
	public HAPInfoElement(HAPReferenceElement eleRef) {
		this.m_eleRef = eleRef;
	}
	
	public HAPIdElement getElementId() {
		HAPIdElement out = null;
		if(this.m_eleRef instanceof HAPAliasElement) {
			HAPAliasElement alias = (HAPAliasElement)this.m_eleRef;
			out = this.m_story.getElementId(alias.getAlias());
			this.setElementId(out);
		}
		else {
			out = (HAPIdElement)this.m_eleRef;
		}
		return out;     
	}
	
	public void setStory(HAPStory story) {  this.m_story = story;    }
	
	private void setElementId(HAPIdElement eleId) {
		this.m_eleRef = eleId;
		this.setId(eleId.toStringValue(HAPSerializationFormat.LITERATE));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		HAPIdElement eleId = new HAPIdElement();
		eleId.buildObject(jsonObj.getJSONObject(ELEMENTID), HAPSerializationFormat.JSON);
		this.setElementId(eleId);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTID, HAPJsonUtility.buildJson(this.m_eleRef, HAPSerializationFormat.JSON));
	}
}
