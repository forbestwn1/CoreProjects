package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPUIResource extends HAPUIResourceBasic{

	//calculated attribute that store all the decendant customer tags within this uiresource
	//with this information, customer tag libs can be loaded when loading ui resource
	Set<String> m_uiTagLibs;
	
	public HAPUIResource(String id){
		super(id);
		this.m_uiTagLibs = new HashSet<String>();
	}
	
	public void addUITagLib(String tag){	this.m_uiTagLibs.add(tag);}

	
	@Override
	protected void buildBasicJsonMap(Map<String, String> jsonMap, String format){
		super.buildBasicJsonMap(jsonMap, format);
		jsonMap.put(HAPAttributeConstant.ATTR_UIRESOURCE_UITAGLIBS, HAPJsonUtility.getSetObjectJson(this.m_uiTagLibs));
	}
		
	@Override
	public String getType() {
		return HAPConstant.CONS_UIRESOURCE_TYPE_RESOURCE;
	}

	@Override
	public void addAttribute(String name, String value){
		super.addAttribute(name, value);
		if(HAPConstant.CONS_UIRESOURCE_ATTRIBUTE_CONTEXT.equals(name)){
			//process "context" attribute, value are multiple data input seperated by ";"
			HAPSegmentParser contextSegs = new HAPSegmentParser(value, HAPConstant.CONS_SEPERATOR_ELEMENT);
			while(contextSegs.hasNext()){
				String varInfo = contextSegs.next();
				HAPSegmentParser varSegs = new HAPSegmentParser(varInfo, HAPConstant.CONS_SEPERATOR_DETAIL);
				String varName = varSegs.next();
				String varType = varSegs.next();
				HAPUIResourceContextInfo contextEleInfo = new HAPUIResourceContextInfo(varName, varType);
				this.addContextInfo(contextEleInfo);
			}
		}
	}
}
