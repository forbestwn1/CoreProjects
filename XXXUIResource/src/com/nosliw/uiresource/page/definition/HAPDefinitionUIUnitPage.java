package com.nosliw.uiresource.page.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;

public class HAPDefinitionUIUnitPage extends HAPDefinitionUIUnit{

	public HAPDefinitionUIUnitPage(String id){
		super(id);
		this.m_source = source;
	}
	
	public String getSource(){   return this.m_source;   }
	
	@Override
	public String getType() {	return HAPConstantShared.UIRESOURCE_TYPE_RESOURCE;	}

	@Override
	public void addAttribute(String name, String value){
		super.addTagAttribute(name, value);
		if(HAPConstantShared.UIRESOURCE_ATTRIBUTE_CONTEXT.equals(name)){
			//process "context" attribute, value are multiple data input seperated by ";"
			HAPSegmentParser contextSegs = new HAPSegmentParser(value, HAPConstantShared.SEPERATOR_ELEMENT);
			while(contextSegs.hasNext()){
				String varInfo = contextSegs.next();
				HAPSegmentParser varSegs = new HAPSegmentParser(varInfo, HAPConstantShared.SEPERATOR_DETAIL);
				String varName = varSegs.next();
				String varType = varSegs.next();
//				HAPUIResourceContextInfo contextEleInfo = new HAPUIResourceContextInfo(varName, varType);
//				this.addContextElement(contextEleInfo);
			}
		}
	}

	@Override
	protected String buildLiterate(){  return this.m_source; }
	
	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
