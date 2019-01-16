package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPParserModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
import com.nosliw.uiresource.processor.HAPProcessorUIResource;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceManager {

	private Map<String, HAPExecutableUIUnitResource> m_uiResource;
	
	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPUITagManager m_uiTagMan;
	
	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerProcess m_processMan;
	
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
		this.m_uiResource = new LinkedHashMap<String, HAPExecutableUIUnitResource>();
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_uiResourceParser = new HAPParserUIResource(null, m_idGengerator);
		this.m_moduleParser = new HAPParserModule(this.m_processMan.getPluginManager());
	}

	public HAPParserModule getModuleParser() {    return this.m_moduleParser;   }
	public HAPParserUIResource getUIResourceParser() {    return this.m_uiResourceParser;  }
	
	public HAPExecutableModule getUIModule(String moduleId) {
		HAPDefinitionModule moduleDef = HAPUtilityUIResource.getUIModuleDefinitionById(moduleId, this.m_moduleParser);
		System.out.println(moduleDef);
		return processModule(moduleDef, moduleId, null);
	}
	
	public HAPExecutableUIUnitResource getUIResource(String uiResourceDefId){
		String id = uiResourceDefId;
		HAPExecutableUIUnitResource out = this.m_uiResource.get(id);
		if(out==null) {
			HAPDefinitionUIUnitResource def = HAPUtilityUIResource.getUIResourceDefinitionById(uiResourceDefId, this.m_uiResourceParser, this);
			out = this.processUIResource(def, id, null);
		}

		System.out.println();
		System.out.println();
		System.out.println("*********************** UI Resource ************************");
		String content = out.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		System.out.println(content);
		System.out.println("*********************** UI Resource ************************");
		System.out.println();
		System.out.println();
		
		return out;
	}

	public HAPExecutableUIUnitResource getUIResource(String uiResourceDefId, String id, HAPContext parentContext){
		HAPDefinitionUIUnitResource def = HAPUtilityUIResource.getUIResourceDefinitionById(uiResourceDefId, this.m_uiResourceParser, this);
		HAPExecutableUIUnitResource out = this.processUIResource(def, id, parentContext);

		System.out.println();
		System.out.println();
		System.out.println("*********************** UI Resource ************************");
		String content = out.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		System.out.println(content);
		System.out.println("*********************** UI Resource ************************");
		System.out.println();
		System.out.println();
		
		return out;
	}

	private HAPExecutableUIUnitResource processUIResource(HAPDefinitionUIUnitResource uiResource, String id, HAPContext parentContext) {
		return HAPProcessorUIResource.processUIResource(uiResource, id, parentContext, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.m_uiResourceParser, m_idGengerator);
	}
	
	private HAPExecutableModule processModule(HAPDefinitionModule uiModule, String id, HAPContextGroup parentContext) {
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(uiModule, id, parentContext, this.m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, processTracker);
	}
}
