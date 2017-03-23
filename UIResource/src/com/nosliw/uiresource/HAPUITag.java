package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPUITag extends HAPUIResourceBasic{

	//name of this customer tag
	private String m_tagName;

	//data bindings related with this customer tag
	private Map<String, HAPDataBinding> m_dataBindings;

	public HAPUITag(String tagName, String id){
		super(id);
		this.m_tagName = tagName;
		this.m_dataBindings = new LinkedHashMap<String, HAPDataBinding>();
	}
	
	public String getTagName(){	return this.m_tagName;}
	
	public void addDataBinding(HAPDataBinding dataBinding){	this.m_dataBindings.put(dataBinding.getName(), dataBinding);	}
	
	@Override
	public String getType() {
		return HAPConstant.UIRESOURCE_TYPE_TAG;
	}

	@Override
	public void addAttribute(String name, String value){
		super.addAttribute(name, value);
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		
		jsonMap.put(HAPAttributeConstant.UIRESOURCE_TAGNAME, this.m_tagName);
		
		Map<String, String> dataBindingJsons = new LinkedHashMap<String, String>();
		for(String name : this.m_dataBindings.keySet()){
			HAPDataBinding dataBinding = this.m_dataBindings.get(name);
			dataBindingJsons.put(name, dataBinding.toStringValue(HAPSerializationFormat.JSON_FULL));
		}
		jsonMap.put(HAPAttributeConstant.UIRESOURCE_DATABINDINGS, HAPJsonUtility.buildMapJson(dataBindingJsons));
	}
}
