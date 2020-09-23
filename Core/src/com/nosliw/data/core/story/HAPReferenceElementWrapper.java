package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPReferenceElementWrapper extends HAPSerializableImp implements HAPWithAlias{

	private HAPIdElement m_elementId;
	
	private HAPAliasElement m_alias;

	public HAPReferenceElementWrapper() {}
	
	public HAPReferenceElementWrapper(HAPReferenceElement elementRef) {
		if(elementRef instanceof HAPIdElement)   this.m_elementId = (HAPIdElement)elementRef;
		else if(elementRef instanceof HAPAliasElement)   this.m_alias = (HAPAliasElement)elementRef;
	}

	public HAPIdElement getElementId() {
		if(m_elementId==null)  throw new RuntimeException();
		return this.m_elementId;
	}
	
	@Override
	public void processAlias(HAPStory story) {
		if(this.m_elementId!=null) {
			story.getElementId(this.m_alias.getName());
		}
	}
	
	public HAPReferenceElementWrapper cloneElementReferenceWrapper() {
		HAPReferenceElementWrapper out = new HAPReferenceElementWrapper();
		if(this.m_alias!=null)   out.m_alias = (HAPAliasElement)this.m_alias.cloneElementReference();
		if(this.m_elementId!=null)   out.m_elementId = (HAPIdElement)this.m_elementId.cloneElementReference();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;

		HAPIdElement eleId = new HAPIdElement();
		eleId.buildObject(jsonObj, HAPSerializationFormat.JSON);
		if(eleId.getCategary()!=null)   this.m_elementId = eleId;
		
		HAPAliasElement alias = new HAPAliasElement();
		alias.buildObject(jsonObj, HAPSerializationFormat.JSON);
		if(alias.getName()!=null)  this.m_alias = alias;
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_alias!=null)  this.m_alias.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_elementId!=null)   this.m_elementId.buildJsonMap(jsonMap, typeJsonMap);
	}
}
