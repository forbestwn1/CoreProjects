package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPUIResourceManager {

	private Map<String, HAPUIDefinitionUnitResource> m_uiResourceDefinitions;
	
	private HAPExpressionManager m_expressionMan; 
	
	private HAPRuntime m_runtime;

	HAPUIResourceIdGenerator m_idGengerator = new HAPUIResourceIdGenerator(1);

	public HAPUIResourceManager(HAPExpressionManager expressionMan, HAPRuntime runtime){
		this.m_expressionMan = expressionMan;
		this.m_runtime = runtime;
		this.m_uiResourceDefinitions = new LinkedHashMap<String, HAPUIDefinitionUnitResource>();
	}

	public HAPUIDefinitionUnitResource addUIResourceDefinition(String file){
		HAPUIResourceParser parser = this.getUIResourceParser();
		HAPUIDefinitionUnitResource resource = parser.parseFile(file);
		resource.calculateConstantDefs(null, m_idGengerator, m_expressionMan, m_runtime);
		this.m_uiResourceDefinitions.put(resource.getId(), resource);
		return resource;
	}
	
	public HAPUIDefinitionUnitResource getUIResourceDefinitionByName(String name){
		return this.m_uiResourceDefinitions.get(name);
	}
	
	public HAPUIResource processUIResource(String name, Map<String, HAPDataTypeCriteria> contextCriteria){
		HAPUIDefinitionUnitResource uiResourceDefinition = this.getUIResourceDefinitionByName(name);
		HAPUIResource uiResource = new HAPUIResource(uiResourceDefinition);
		uiResource.process(this.m_expressionMan);
		
		return uiResource;
	}
	
	private HAPUIResourceParser getUIResourceParser(){
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, m_idGengerator);
		return uiResourceParser;
	}
}
