package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.definition.HAPContext;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;

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

    //Add resource definition from file 
	public HAPUIDefinitionUnitResource addUIResourceDefinition(String file){
		HAPUIDefinitionUnitResource resource = this.getUIResourceParser().parseFile(file);
		resource.calculateConstantDefs(null, m_idGengerator, m_expressionMan, m_runtime);
		this.m_uiResourceDefinitions.put(resource.getId(), resource);
		return resource;
	}
	
	/**
	 * Add resource definition by overriding the existing context 
	 * @param base     name of base definition
	 * @param context  new context to apply
	 * @return
	 */
	public HAPUIDefinitionUnitResource addUIResourceDefinition(String resourceId, String base, HAPContext context){
		String baseContent = this.getUIResourceDefinitionByName(base).getSource();
		//build resource using base resource
		HAPUIDefinitionUnitResource resource = this.getUIResourceParser().parseContent(resourceId, baseContent);
		resource.calculateConstantDefs(null, m_idGengerator, m_expressionMan, m_runtime);
		
		//update context with new context
		resource.getContext().hardMergeWith(context);
		
		this.m_uiResourceDefinitions.put(resource.getId(), resource);
		return resource;
	}
	
	public HAPUIDefinitionUnitResource getUIResourceDefinitionByName(String name){
		return this.m_uiResourceDefinitions.get(name);
	}
	
	public HAPUIResource getUIResource(String name){
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
