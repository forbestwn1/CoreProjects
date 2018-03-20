package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.context.HAPContextGroup;
import com.nosliw.uiresource.context.HAPContextUtility;
import com.nosliw.uiresource.definition.HAPConstantUtility;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionProcessorUtility;
import com.nosliw.uiresource.parser.HAPUIResourceParser;
import com.nosliw.uiresource.resource.HAPResourceUtility;
import com.nosliw.uiresource.tag.HAPUITagManager;
import com.nosliw.uiresource.tag.HAPUITagUtility;

public class HAPUIResourceManager {

	private Map<String, HAPUIDefinitionUnitResource> m_uiResourceDefinitions;
	
	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPUITagManager m_uiTagMan;
	
	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	public HAPUIResourceManager(
			HAPUITagManager uiTagMan,
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper){
		this.m_uiTagMan = uiTagMan;
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_runtime = runtime;
		this.m_uiResourceDefinitions = new LinkedHashMap<String, HAPUIDefinitionUnitResource>();
		this.m_dataTypeHelper = dataTypeHelper;
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
	public HAPUIDefinitionUnitResource addUIResourceDefinition(String resourceId, String base, HAPContextGroup context){
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
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceDefinitions.get(name);
		if(uiResource==null){
			//if not registered, then process uiResource on the fly
			String file = HAPFileUtility.getUIResourceFolder()+name+".res";
			uiResource = this.getUIResourceParser().parseFile(file);
			HAPConstantUtility.calculateConstantDefs(uiResource, null, m_idGengerator, m_expressionMan, m_runtime);
		}
		return uiResource;
	}
	
	public HAPUIDefinitionUnitResource getUIResource(String name){
		HAPUIDefinitionUnitResource uiResource = this.getUIResourceDefinitionByName(name);
		if(!uiResource.isProcessed()){
			//process include tags
			HAPUITagUtility.processIncludeTags(uiResource, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, getUIResourceParser(), this.m_idGengerator);
			
			//build expression context
			HAPContextUtility.processExpressionContext(null, uiResource, this.m_dataTypeHelper, this.m_uiTagMan, this.m_runtime, this.m_expressionMan);

			//process expression definition
			HAPUIResourceExpressionProcessorUtility.processExpressions(uiResource, m_runtime, m_resourceMan);
			
			//discovery resources required
			HAPResourceUtility.processResourceDependency(uiResource, m_resourceMan);
			uiResource.processed();
			
			System.out.println("********************** "+  name  +"  ******************************");
			System.out.println(uiResource);
			System.out.println("**********************   ******************************");
		}
		return uiResource;
	}
	
	private HAPUIResourceParser getUIResourceParser(){
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, m_idGengerator);
		return uiResourceParser;
	}
}
