package com.nosliw.uiresource;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.context.HAPContextGroup;
import com.nosliw.uiresource.module.HAPUIModuleEntry;
import com.nosliw.uiresource.module.HAPDefinitionUIModule;
import com.nosliw.uiresource.module.HAPDefinitionUIModuleEntry;
import com.nosliw.uiresource.page.HAPConstantUtility;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.parser.HAPUIResourceParser;
import com.nosliw.uiresource.processor.HAPUIResourceProcessor;
import com.nosliw.uiresource.tag.HAPUITagManager;

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

	
	public HAPUIModuleEntry getUIModuleInstance(String moduleId, String entry) {
		HAPDefinitionUIModule moduleDef = this.getUIModuleById(moduleId);
		
		HAPDefinitionUIModuleEntry moduleEntry = moduleDef.getModuleEntry(entry);
		HAPUIModuleEntry out = new HAPUIModuleEntry(moduleEntry.getPage());
		
		Map<String, String> pages = moduleDef.getPages();
		for(String pageName : pages.keySet()) {
			out.addPage(pageName, this.getUIResource(pages.get(pageName)));
			out.addPageUIResourceName(pageName, pages.get(pageName));
		}
		
		return out;
	}
	
	private HAPDefinitionUIModule getUIModuleById(String moduleId) {
		String file = HAPFileUtility.getUIModuleFolder()+moduleId+".res";
		HAPDefinitionUIModule out = (HAPDefinitionUIModule)HAPSerializeManager.getInstance().buildObject(HAPDefinitionUIModule.class.getName(), new JSONObject(HAPFileUtility.readFile(new File(file))), HAPSerializationFormat.JSON);
		out.setId(moduleId);
		return out;
	}
	
	
    //Add resource definition from file 
	public HAPUIDefinitionUnitResource addUIResourceDefinition(String file){
		HAPUIDefinitionUnitResource resource = this.readUiResourceDefinitionFromFile(file);
		this.m_uiResourceDefinitions.put(resource.getId(), resource);
		return resource;
	}
	
	public HAPUIDefinitionUnitResource getUIResourceDefinitionById(String id){
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceDefinitions.get(id);
		if(uiResource==null){
			//if not registered, then process uiResource on the fly
			String file = HAPFileUtility.getUIResourceFolder()+id+".res";
			uiResource = this.readUiResourceDefinitionFromFile(file);
		}
		return uiResource;
	}
	
	/**
	 * Add resource definition by overriding the existing context 
	 * @param definitionId     name of base definition
	 * @param context  new context to apply
	 * @return
	 */
	public HAPUIDefinitionUnitResource getUIResource(String resourceId, String definitionId, HAPContextGroup context){
		String baseContent = this.getUIResourceDefinitionById(definitionId).getSource();
		//build resource using base resource
		HAPUIDefinitionUnitResource resource = this.getUIResourceParser().parseContent(resourceId, baseContent);
		HAPConstantUtility.calculateConstantDefs(resource, null, m_idGengerator, m_expressionMan, m_runtime);
		
		//update context with new context
		resource.getContext().hardMergeWith(context);

		this.processUIResource(resource);
		
		return resource;
	}
	
	public HAPUIDefinitionUnitResource getUIResource(String id){
		HAPUIDefinitionUnitResource uiResource = this.getUIResourceDefinitionById(id);
		if(!uiResource.isProcessed()){
			this.processUIResource(uiResource);
		}
		return uiResource;
	}
	
	private void processUIResource(HAPUIDefinitionUnitResource uiResource) {
		HAPUIResourceProcessor.processUIResource(uiResource, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.getUIResourceParser(), m_idGengerator);
	}
	
	private HAPUIDefinitionUnitResource readUiResourceDefinitionFromFile(String file) {
		HAPUIDefinitionUnitResource uiResource = this.getUIResourceParser().parseFile(file);
		HAPConstantUtility.calculateConstantDefs(uiResource, null, m_idGengerator, m_expressionMan, m_runtime);
		return uiResource;
	}
	
	private HAPUIResourceParser getUIResourceParser(){
		HAPUIResourceParser uiResourceParser = new HAPUIResourceParser(null, this.m_expressionMan, m_idGengerator);
		return uiResourceParser;
	}
}
