package com.nosliw.entity.definition.xmlimp;

import org.w3c.dom.Element;

import com.nosliw.common.keyvalue.HAPKeyValueXml;


public class HAPEntityDefinitionMeta extends HAPKeyValueXml
{
	public HAPEntityDefinitionMeta(){}

	public HAPEntityDefinitionMeta(Element ele){
		this.readAllAttributes(ele);
	}
	
	public String getClassPrefix(){
		String out = this.getValue(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_META_CLASSPREFIX);
		if(out!=null)  return out;
		return "";
	}
	public String getNamePrefix(){
		String out = this.getValue(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_META_NAMEPREFIX);
		if(out!=null)  return out;
		return "";
	}
}
