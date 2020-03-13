package com.nosliw.uiresource;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.common.HAPEntityOrReference;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.expression.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.resource.HAPResourceCache;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPDefinitionAppEntry;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;
import com.nosliw.uiresource.application.HAPParseMiniApp;
import com.nosliw.uiresource.application.HAPProcessMiniAppEntry;
import com.nosliw.uiresource.common.HAPIdGenerator;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPParserModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
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
	
	private HAPManagerProcess m_processMan;
	
	private HAPManagerServiceDefinition m_serviceDefinitionManager;
	
	private HAPManagerResourceDefinition m_resourceDefManager;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	private HAPParserPage m_uiResourceParser;
	
	private HAPParserModule m_moduleParser;

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPUIResourceManager(
			HAPUITagManager uiTagMan,
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan,
			HAPManagerProcess processMan,
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPManagerResourceDefinition resourceDefManager){
		this.m_uiTagMan = uiTagMan;
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_processMan = processMan;
		this.m_runtime = runtime;
		this.m_resourceCache = new HAPResourceCache();
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_uiResourceParser = new HAPParserPage(null, m_idGengerator);
		this.m_moduleParser = new HAPParserModule();
		this.m_miniAppParser = new HAPParseMiniApp();
		this.m_serviceDefinitionManager = serviceDefinitionManager;
		this.m_resourceDefManager = resourceDefManager;
	}

	public HAPDefinitionApp getMiniAppDefinition(HAPResourceId appId, HAPAttachmentContainer parentAttachment) {
		//get definition itself
		HAPDefinitionApp appDef = (HAPDefinitionApp)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(appId, parentAttachment);
		return appDef;
	}
	
	public HAPDefinitionAppEntry getMiniAppEntryDefinition(HAPResourceId appEntryId, HAPAttachmentContainer parentAttachment) {
		HAPDefinitionAppEntry appEntryDef = (HAPDefinitionAppEntry)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(appEntryId, parentAttachment);
		return appEntryDef;
	}

	public HAPExecutableAppEntry getMiniAppEntry(HAPResourceId appEntryId) {
		return this.getEmbededMiniAppEntry(appEntryId, null, null);
	}
	
	public HAPExecutableAppEntry getEmbededMiniAppEntry(HAPResourceId appEntryId, HAPAttachmentContainer parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		HAPDefinitionAppEntry appEntryDef = this.getMiniAppEntryDefinition(appEntryId, attachmentEx);
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		HAPExecutableAppEntry out = HAPProcessMiniAppEntry.process(appEntryDef, null, m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager, processTracker);
		return out;
	}
	
	public HAPDefinitionModule getModuleDefinition(HAPResourceId moduleId, HAPAttachmentContainer parentAttachment) {
		//get definition itself
		HAPDefinitionModule moduleDef = (HAPDefinitionModule)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(moduleId, parentAttachment);
		return moduleDef;
	}

	public HAPExecutableModule getUIModule(HAPResourceId moduleId) {
		HAPExecutableModule out = (HAPExecutableModule)this.m_resourceCache.getResource(moduleId);
		if(out==null) {
			out = getEmbededUIModule(moduleId, null, null);
		}
		return out;
	}

	public HAPExecutableModule getEmbededUIModule(HAPEntityOrReference defOrRef, HAPAttachmentContainer parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionModule moduleDef = null;
		String id = null;
		HAPAttachmentContainer attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId moduleId = (HAPResourceId)defOrRef;
			moduleDef = getModuleDefinition(moduleId, attachmentEx);
			id = moduleId.getIdLiterate();
		}
		else if(defOrRef instanceof HAPResourceDefinition) {
			moduleDef = (HAPDefinitionModule)defOrRef;
			HAPUtilityComponent.mergeWithParentAttachment(moduleDef, attachmentEx);
		}
		return HAPProcessorModule.process(moduleDef, id, null, m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager);
	}

	
	public HAPExecutableUIUnitPage getUIPage(HAPResourceId pageResourceId){
		HAPExecutableUIUnitPage out = (HAPExecutableUIUnitPage)this.m_resourceCache.getResource(pageResourceId);
		if(out==null) {
			out = getEmbededUIPage(pageResourceId, pageResourceId.getIdLiterate(), null, null, null, null);
		}
		return out;
	}
	
	public HAPDefinitionUIPage getUIPageDefinition(HAPResourceId pageResourceId, HAPAttachmentContainer parentAttachment) {
		
		HAPDefinitionUIPage pageDefinition = (HAPDefinitionUIPage)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(pageResourceId, parentAttachment);

		//process include tag
		pageDefinition = HAPUtilityPage.processInclude(pageDefinition, this.m_uiResourceParser, this, this.m_resourceDefManager);

		//resolve attachment
		HAPUtilityPage.solveAttachment(pageDefinition, null, this.m_uiTagMan);

		//resolve service provider
		HAPUtilityPage.solveServiceProvider(pageDefinition, null, m_serviceDefinitionManager);
		return pageDefinition;
	}

	public HAPExecutableUIUnitPage getEmbededUIPage(HAPEntityOrReference defOrRef, String id, HAPContextGroup context, HAPContextGroup parentContext, HAPAttachmentContainer parentAttachment, HAPWithNameMapping withNameMapping){
		HAPDefinitionUIPage pageDef = null;
		HAPAttachmentContainer attachmentEx = null;
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId pageId = (HAPResourceId)defOrRef;
			attachmentEx = HAPUtilityComponent.buildInternalAttachment(pageId, parentAttachment, withNameMapping);
			pageDef = getUIPageDefinition(pageId, attachmentEx);
		}
		
		//compile it
		HAPExecutableUIUnitPage out = HAPProcessorUIPage.processUIResource(pageDef, id, context, parentContext, null, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.m_uiResourceParser, this.m_serviceDefinitionManager, m_idGengerator);
		return out;
	}

	public HAPParserModule getModuleParser() {    return this.m_moduleParser;   }
	public HAPParserPage getUIResourceParser() {    return this.m_uiResourceParser;  }
	public HAPParseMiniApp getMinitAppParser() {    return this.m_miniAppParser;     }
}
