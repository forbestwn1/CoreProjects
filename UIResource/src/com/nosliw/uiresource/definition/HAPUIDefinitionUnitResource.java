package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;

public class HAPUIDefinitionUnitResource extends HAPUIDefinitionUnit{

	//calculated attribute that store all the decendant customer tags within this uiresource
	//with this information, customer tag libs can be loaded when loading ui resource
	Set<String> m_uiTagLibs;
	
	//source code of resource definition
	private String m_source;

	private boolean m_processed = false;
	
	//all dependency resources
	private List<HAPResourceDependent> m_resourceDependency;
	
	public HAPUIDefinitionUnitResource(String id, String source){
		super(id);
		this.m_source = source;
		this.m_uiTagLibs = new HashSet<String>();
		this.m_resourceDependency = new ArrayList<HAPResourceDependent>();
	}
	
	public void process(HAPExpressionManager expressionManager, HAPResourceManagerRoot resourceMan){
		this.processExpressions(null, expressionManager);
		this.processResourceDependency(resourceMan);
		this.m_processed = true;
	}
	
	private void processResourceDependency(HAPResourceManagerRoot resourceMan){
		 Set<HAPResourceId> dependencyResourceIds = new LinkedHashSet();
		
		//resources need by expression
		List<HAPExpression> expressions = HAPUIResourceUtility.discoverExpressionsInUIResource(this);
		for(HAPExpression exp : expressions){
			List<HAPResourceId> expressionDependency = HAPExpressionUtility.discoverResources(exp);
			dependencyResourceIds.addAll(expressionDependency);
		}
		
		//resource need by tag
		
		Iterator<HAPResourceId> it = dependencyResourceIds.iterator();
		while(it.hasNext()){
			HAPResourceId resourceId = it.next();
			this.m_resourceDependency.add(new HAPResourceDependent(resourceId));
		}
	}
	
	public void addUITagLib(String tag){	this.m_uiTagLibs.add(tag);}

	public String getSource(){   return this.m_source;   }
	
	public boolean isProcessed(){  return this.m_processed;  }
	
	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
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
