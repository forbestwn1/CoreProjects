package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPDefinitionUIUnitPage extends HAPDefinitionUIUnit{

	//source code of resource definition
	private String m_source;
	
	public HAPDefinitionUIUnitPage(String id, String source){
		super(id);
		this.m_source = source;
	}
	
	public String getSource(){   return this.m_source;   }
	
	@Override
	public String getType() {	return HAPConstant.UIRESOURCE_TYPE_RESOURCE;	}

	@Override
	public void addAttribute(String name, String value){
		super.addAttribute(name, value);
		if(HAPConstant.UIRESOURCE_ATTRIBUTE_CONTEXT.equals(name)){
			//process "context" attribute, value are multiple data input seperated by ";"
			HAPSegmentParser contextSegs = new HAPSegmentParser(value, HAPConstant.SEPERATOR_ELEMENT);
			while(contextSegs.hasNext()){
				String varInfo = contextSegs.next();
				HAPSegmentParser varSegs = new HAPSegmentParser(varInfo, HAPConstant.SEPERATOR_DETAIL);
				String varName = varSegs.next();
				String varType = varSegs.next();
//				HAPUIResourceContextInfo contextEleInfo = new HAPUIResourceContextInfo(varName, varType);
//				this.addContextElement(contextEleInfo);
			}
		}
	}
}
