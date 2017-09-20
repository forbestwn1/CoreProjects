package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPUIResourceManager {

	private Map<String, HAPUIResource> m_uiResources;
	
	private HAPExpressionManager m_expressionMan; 
	
	private HAPRuntime m_runtime;

	HAPUIResourceIdGenerator m_idGengerator = new HAPUIResourceIdGenerator(1);

	public HAPUIResourceManager(HAPExpressionManager expressionMan, HAPRuntime runtime){
		this.m_expressionMan = expressionMan;
		this.m_runtime = runtime;
		this.m_uiResources = new LinkedHashMap<String, HAPUIResource>();
	}

	public HAPUIResource addUIResource(String file){
		HAPUIResourceParser parser = this.getUIResourceParser();
		HAPUIResource resource = parser.parseFile(file);
		resource.processConstants(null, m_idGengerator, m_expressionMan, m_runtime);
		this.m_uiResources.put(resource.getId(), resource);
		return resource;
	}
	
	public HAPUIResource getResourceByName(String name){
		return this.m_uiResources.get(name);
	}
	
	public HAPUIResource processUIResource(String resourceName, Map<String, HAPDataTypeCriteria> expectedCriteria){
		
		
		
		HAPUIResourceParser uiResourceParser = this.getUIResourceParser();
		HAPUIResource uiResource = uiResourceParser.parseFile(file);
		
		Map<String, HAPConstantDef> constantDefs = uiResource.getConstants();
		for(String name : constantDefs.keySet()){
			HAPConstantDef constantDef = constantDefs.get(name);
			constantDef.process(constantDefs, idGengerator, m_expressionMan, this.m_runtime);
		}
		return uiResource;
	}
	
	private HAPUIResourceParser getUIResourceParser(){
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, m_idGengerator);
		return uiResourceParser;
	}
}
