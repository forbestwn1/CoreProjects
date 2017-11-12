package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.context.HAPContextUtility;
import com.nosliw.uiresource.definition.HAPConstantUtility;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.definition.HAPUIResourceUtility;
import com.nosliw.uiresource.resource.HAPResourceUtility;

public class HAPUIResourceManager {

	private Map<String, HAPUIDefinitionUnitResource> m_uiResourceDefinitions;
	
	private HAPExpressionManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;
	
	private HAPRuntime m_runtime;

	HAPUIResourceIdGenerator m_idGengerator = new HAPUIResourceIdGenerator(1);

	public HAPUIResourceManager(HAPExpressionManager expressionMan, HAPResourceManagerRoot resourceMan, HAPRuntime runtime){
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_runtime = runtime;
		this.m_uiResourceDefinitions = new LinkedHashMap<String, HAPUIDefinitionUnitResource>();
	}

    //Add resource definition from file 
	public HAPUIDefinitionUnitResource addUIResourceDefinition(String file){
		HAPUIDefinitionUnitResource resource = this.getUIResourceParser().parseFile(file);
		HAPConstantUtility.calculateConstantDefs(resource, null, m_idGengerator, m_expressionMan, m_runtime);
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
		HAPConstantUtility.calculateConstantDefs(resource, null, m_idGengerator, m_expressionMan, m_runtime);
		
		//update context with new context
		resource.getContext().hardMergeWith(context);
		
		this.m_uiResourceDefinitions.put(resource.getId(), resource);
		return resource;
	}
	
	public HAPUIDefinitionUnitResource getUIResourceDefinitionByName(String name){
		return this.m_uiResourceDefinitions.get(name);
	}
	
	public HAPUIDefinitionUnitResource getUIResource(String name){
		HAPUIDefinitionUnitResource uiResource = this.getUIResourceDefinitionByName(name);
		if(uiResource==null){
			//if not registered, then process uiResource on the fly
			String file = "C:\\Users\\ewaniwa\\Desktop\\MyWork\\ApplicationData\\uiresources\\"+name+".res";
			uiResource = this.getUIResourceParser().parseFile(file);
			HAPConstantUtility.calculateConstantDefs(uiResource, null, m_idGengerator, m_expressionMan, m_runtime);
		}
		if(!uiResource.isProcessed()){
			//build expression context
			HAPContextUtility.processExpressionContext(null, uiResource);

			
			HAPUIResourceUtility.processUIResource(uiResource, m_runtime, m_resourceMan);
			
			//discovery resources required
			HAPResourceUtility.processResourceDependency(uiResource, m_resourceMan);
			uiResource.processed();
		}
		return uiResource;
	}
	
	private HAPUIResourceParser getUIResourceParser(){
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, m_idGengerator);
		return uiResourceParser;
	}
}
