package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPParserModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.processor.HAPProcessorUIPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUIResourceManager {

	private Map<String, HAPExecutableUIUnitPage> m_uiResource;
	
	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPUITagManager m_uiTagMan;
	
	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerProcess m_processMan;
	
	HAPManagerServiceDefinition m_serviceDefinitionManager;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	private HAPParserUIResource m_uiResourceParser;
	
	private HAPParserModule m_moduleParser;

	public HAPUIResourceManager(
			HAPUITagManager uiTagMan,
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan,
			HAPManagerProcess processMan,
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper){
		this.m_uiTagMan = uiTagMan;
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_processMan = processMan;
		this.m_runtime = runtime;
		this.m_uiResource = new LinkedHashMap<String, HAPExecutableUIUnitPage>();
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_uiResourceParser = new HAPParserUIResource(null, m_idGengerator);
		this.m_moduleParser = new HAPParserModule(this.m_processMan.getPluginManager());
	}

	public HAPParserModule getModuleParser() {    return this.m_moduleParser;   }
	public HAPParserUIResource getUIResourceParser() {    return this.m_uiResourceParser;  }
	
	public HAPExecutableModule getUIModule(String moduleId) {
		HAPDefinitionModule moduleDef = HAPUtilityUIResource.getUIModuleDefinitionById(moduleId, this.m_moduleParser);
		return processModule(moduleDef, moduleId, null);
	}
	
	public HAPExecutableUIUnitPage getUIResource(String uiResourceDefId){
		String id = uiResourceDefId;
		HAPExecutableUIUnitPage out = this.m_uiResource.get(id);
		if(out==null) {
			out = this.getUIResource(uiResourceDefId, id, null, null);
//			HAPDefinitionUIUnitPage def = HAPUtilityUIResource.getUIResourceDefinitionById(uiResourceDefId, this.m_uiResourceParser, this);
//			out = this.processUIResource(def, id, null, null);
		}
		return out;
	}

	public HAPExecutableUIUnitPage getUIResource(String uiResourceDefId, String id, HAPContextGroup context, HAPContextGroup parentContext){
		HAPDefinitionUIPage def = HAPUtilityUIResource.getUIResourceDefinitionById(uiResourceDefId, this.m_uiResourceParser, this);
		HAPExecutableUIUnitPage out = this.processUIResource(def, id, context, parentContext);
		return out;
	}

	private HAPExecutableUIUnitPage processUIResource(HAPDefinitionUIPage uiResource, String id, HAPContextGroup context, HAPContextGroup parentContext) {
		return HAPProcessorUIPage.processUIResource(uiResource, id, context, parentContext, null, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.m_uiResourceParser, this.m_serviceDefinitionManager, m_idGengerator);
	}
	
	private HAPExecutableModule processModule(HAPDefinitionModule uiModule, String id, HAPContextGroup parentContext) {
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(uiModule, id, parentContext, this.m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, this.m_serviceDefinitionManager, processTracker);
	}
}
