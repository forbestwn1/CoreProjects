package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.runtime.HAPResourceDependent;

public class HAPUIDefinitionUnitResource extends HAPUIDefinitionUnit{

	@HAPAttribute
	public static final String CONTEXT = "context";
	
	//context definition
	private HAPContext m_context;
	
	//source code of resource definition
	private String m_source;
	
	
	//calculated attribute that store all the decendant customer tags within this uiresource
	//with this information, customer tag libs can be loaded when loading ui resource
	Set<String> m_uiTagLibs;
	


	
	private boolean m_processed = false;
	
	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;
	
	public HAPUIDefinitionUnitResource(String id, String source){
		super(id);
		this.m_context = new HAPContext();
		this.m_source = source;
		this.m_uiTagLibs = new HashSet<String>();
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
	}
	
	public void addUITagLib(String tag){	this.m_uiTagLibs.add(tag);}

	public String getSource(){   return this.m_source;   }
	public HAPContext getContext(){  return this.m_context;  }
	
	public boolean isProcessed(){  return this.m_processed;  }
	public void processed(){  this.m_processed = true;  }
	
	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(m_context, HAPSerializationFormat.JSON_FULL));
		jsonMap.put(UITAGLIBS, HAPJsonUtility.buildJson(this.m_uiTagLibs, HAPSerializationFormat.JSON_FULL));
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
