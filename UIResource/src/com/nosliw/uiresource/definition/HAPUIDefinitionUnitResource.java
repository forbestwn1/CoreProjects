package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.runtime.HAPResourceDependent;

public class HAPUIDefinitionUnitResource extends HAPUIDefinitionUnit{

	//source code of resource definition
	private String m_source;
	
	
	private boolean m_processed = false;
	
	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;
	
	public HAPUIDefinitionUnitResource(String id, String source){
		super(id);
		this.m_source = source;
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
	}
	
	public String getSource(){   return this.m_source;   }
	
	public boolean isProcessed(){  return this.m_processed;  }
	public void processed(){  this.m_processed = true;  }
	
	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public String getType() {
		return HAPConstant.UIRESOURCE_TYPE_RESOURCE;
	}

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
