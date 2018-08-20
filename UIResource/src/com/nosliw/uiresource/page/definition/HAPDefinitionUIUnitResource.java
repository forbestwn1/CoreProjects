package com.nosliw.uiresource.page.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.script.context.HAPContextEntity;

public class HAPDefinitionUIUnitResource extends HAPDefinitionUIUnit{

	//source code of resource definition
	private String m_source;
	
	private Map<String, HAPContextEntity> m_commandsDefinition;
	
	public HAPDefinitionUIUnitResource(String id, String source){
		super(id);
		this.m_source = source;
		this.m_commandsDefinition = new LinkedHashMap<String, HAPContextEntity>();
	}
	
	public String getSource(){   return this.m_source;   }
	
	public void addCommandDefinition(HAPContextEntity commandDef) {   this.m_commandsDefinition.put(commandDef.getName(), commandDef);   }
	public Map<String, HAPContextEntity> getCommandDefinition() {   return this.m_commandsDefinition;  }

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
