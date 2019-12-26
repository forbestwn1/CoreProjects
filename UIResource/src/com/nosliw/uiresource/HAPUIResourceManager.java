package com.nosliw.uiresource;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPComponentUtility;
import com.nosliw.data.core.component.HAPDefinitionExternalMapping;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.resource.HAPResourceCache;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.provide.HAPUtilityService;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;
import com.nosliw.uiresource.application.HAPParseMiniApp;
import com.nosliw.uiresource.application.HAPProcessMiniAppEntry;
import com.nosliw.uiresource.application.HAPUtilityApp;
import com.nosliw.uiresource.common.HAPIdGenerator;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPParserModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
import com.nosliw.uiresource.module.HAPUtilityModule;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.definition.HAPParserPage;
import com.nosliw.uiresource.page.definition.HAPUtilityPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.processor.HAPProcessorUIPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUIResourceManager {

	private HAPResourceCache m_resourceCache;
	
	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPUITagManager m_uiTagMan;
	
	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerProcessDefinition m_processMan;
	
	private HAPManagerServiceDefinition m_serviceDefinitionManager;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	private HAPParserPage m_uiResourceParser;
	
	private HAPParserModule m_moduleParser;

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPUIResourceManager(
			HAPUITagManager uiTagMan,
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan,
			HAPManagerProcessDefinition processMan,
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerServiceDefinition serviceDefinitionManager){
		this.m_uiTagMan = uiTagMan;
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_processMan = processMan;
		this.m_runtime = runtime;
		this.m_resourceCache = new HAPResourceCache();
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_uiResourceParser = new HAPParserPage(null, m_idGengerator);
		this.m_moduleParser = new HAPParserModule(this.m_processMan.getPluginManager());
		this.m_miniAppParser = new HAPParseMiniApp(this.m_processMan.getPluginManager());
		this.m_serviceDefinitionManager = serviceDefinitionManager;
	}

	public HAPExecutableAppEntry getMiniApp(String appId, String entry, HAPDefinitionExternalMapping parentExternalMapping) {
		HAPDefinitionApp miniAppDef = HAPUtilityApp.getAppDefinitionById(appId, this.m_miniAppParser);
		//resolve external mapping for app
		HAPComponentUtility.solveExternalMapping(miniAppDef, parentExternalMapping);
		//resolve external mapping for entry
		HAPComponentUtility.solveExternalMapping(miniAppDef.getEntry(entry), miniAppDef.getExternalMapping());
		//resolve service provider for app
		HAPUtilityService.solveServiceProvider(miniAppDef, null, parentExternalMapping, null, m_serviceDefinitionManager);
		//resolve service provider for entry
		HAPUtilityService.solveServiceProvider(miniAppDef.getEntry(entry), miniAppDef, miniAppDef.getExternalMapping(), null, m_serviceDefinitionManager);
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		HAPExecutableAppEntry out = HAPProcessMiniAppEntry.process(miniAppDef, entry, null, m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager, processTracker);
		return out;
	}
	
	public HAPExecutableModule getUIModule(String moduleId, HAPDefinitionExternalMapping parentExternalMapping) {
		HAPDefinitionModule moduleDef = HAPUtilityModule.getUIModuleDefinitionById(moduleId, this.m_moduleParser);
		//resolve external mapping
		HAPComponentUtility.solveExternalMapping(moduleDef, parentExternalMapping);
		//resolve service provider
		HAPUtilityService.solveServiceProvider(moduleDef, null, moduleDef.getExternalMapping(), null, m_serviceDefinitionManager);
		return processModule(moduleDef, moduleId, null);
	}
	
	public HAPExecutableUIUnitPage getUIPage(String uiResourceDefId, HAPDefinitionExternalMapping parentExternalMapping){
		String id = uiResourceDefId;
		HAPExecutableUIUnitPage out = (HAPExecutableUIUnitPage)this.m_resourceCache.getResource(id, parentExternalMapping==null?null:parentExternalMapping.toResourceIdSupplement());
		if(out==null) {
			out = this.getUIPage(uiResourceDefId, id, null, null, parentExternalMapping);
		}
		return out;
	}

	public HAPExecutableUIUnitPage getUIPage(String uiResourceDefId, String id, HAPContextGroup context, HAPContextGroup parentContext, HAPDefinitionExternalMapping parentExternalMapping){
		//get definition itself
		HAPDefinitionUIPage def = HAPUtilityPage.getPageDefinitionById(uiResourceDefId, this.m_uiResourceParser, this);
		//resolve external mapping
		HAPUtilityPage.solveExternalMapping(def, parentExternalMapping, this.m_uiTagMan);
		//resolve service provider
		HAPUtilityPage.solveServiceProvider(def, null, m_serviceDefinitionManager);
		//compile it
		HAPExecutableUIUnitPage out = this.processUIResource(def, id, context, parentContext);
		return out;
	}

	public HAPParserModule getModuleParser() {    return this.m_moduleParser;   }
	public HAPParserPage getUIResourceParser() {    return this.m_uiResourceParser;  }
	
	private HAPExecutableUIUnitPage processUIResource(HAPDefinitionUIPage uiResource, String id, HAPContextGroup context, HAPContextGroup parentContext) {
		return HAPProcessorUIPage.processUIResource(uiResource, id, context, parentContext, null, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.m_uiResourceParser, this.m_serviceDefinitionManager, m_idGengerator);
	}
	
	private HAPExecutableModule processModule(HAPDefinitionModule uiModule, String id, HAPContextGroup parentContext) {
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(uiModule, id, parentContext, null, this.m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, this.m_serviceDefinitionManager, processTracker);
	}
}
