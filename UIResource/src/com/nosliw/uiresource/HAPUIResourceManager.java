package com.nosliw.uiresource;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.resource.HAPResourceCache;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPParserElement;
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
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.processor.HAPProcessorUIPage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIData;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIHtml;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUITagOther;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPUIResourceManager {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPResourceCache m_resourceCache;
	
	private HAPManagerUITag m_uiTagMan;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	private HAPParserPage m_uiResourceParser;
	
	private HAPParserModule m_moduleParser;

	private HAPParseMiniApp m_miniAppParser;
	
	public HAPUIResourceManager(
			HAPRuntimeEnvironment runtimeEnv,
			HAPManagerUITag uiTagMan
			){
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagMan = uiTagMan;
		this.m_resourceCache = new HAPResourceCache();
		this.m_uiResourceParser = new HAPParserPage(null, m_idGengerator, runtimeEnv);
		this.m_moduleParser = new HAPParserModule();
		this.m_miniAppParser = new HAPParseMiniApp();

		HAPParserElement.registerStoryNode(HAPStoryNodePage.STORYNODE_TYPE, HAPStoryNodePage.class);
		HAPParserElement.registerStoryNode(HAPStoryNodeUIData.STORYNODE_TYPE, HAPStoryNodeUIData.class);
		HAPParserElement.registerStoryNode(HAPStoryNodeUIHtml.STORYNODE_TYPE, HAPStoryNodeUIHtml.class);
		HAPParserElement.registerStoryNode(HAPStoryNodeUITagOther.STORYNODE_TYPE, HAPStoryNodeUITagOther.class);
	}

	public HAPDefinitionApp getMiniAppDefinition(HAPResourceId appId, HAPContainerAttachment parentAttachment) {
		//get definition itself
		HAPDefinitionApp appDef = (HAPDefinitionApp)this.m_runtimeEnv.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(appId, parentAttachment);
		return appDef;
	}
	
	public HAPDefinitionAppEntry getMiniAppEntryDefinition(HAPResourceId appEntryId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionAppEntry appEntryDef = (HAPDefinitionAppEntry)this.m_runtimeEnv.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(appEntryId, parentAttachment);
		return appEntryDef;
	}

	public HAPExecutableAppEntry getMiniAppEntry(HAPResourceId appEntryId) {
		return this.getEmbededMiniAppEntry(appEntryId, null, null);
	}
	
	public HAPExecutableAppEntry getEmbededMiniAppEntry(HAPResourceId appEntryId, HAPContainerAttachment parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPContainerAttachment attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		HAPDefinitionAppEntry appEntryDef = this.getMiniAppEntryDefinition(appEntryId, attachmentEx);
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		HAPExecutableAppEntry out = HAPProcessMiniAppEntry.process(appEntryDef, null, this.m_runtimeEnv, this, processTracker);
		return out;
	}
	
	public HAPDefinitionModule getModuleDefinition(HAPResourceId moduleId, HAPContainerAttachment parentAttachment) {
		//get definition itself
		HAPDefinitionModule moduleDef = (HAPDefinitionModule)this.m_runtimeEnv.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(moduleId, parentAttachment);
		return moduleDef;
	}

	public HAPExecutableModule getUIModule(HAPResourceId moduleId) {
		HAPExecutableModule out = (HAPExecutableModule)this.m_resourceCache.getResource(moduleId);
		if(out==null) {
			out = getEmbededUIModule(moduleId, null, null);
		}
		return out;
	}

	public HAPExecutableModule getEmbededUIModule(HAPEntityOrReference defOrRef, HAPContainerAttachment parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionModule moduleDef = null;
		String id = null;
		HAPContainerAttachment attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId moduleId = (HAPResourceId)defOrRef;
			moduleDef = getModuleDefinition(moduleId, attachmentEx);
			id = moduleId.getCoreIdLiterate();
		}
		else if(defOrRef instanceof HAPResourceDefinition) {
			moduleDef = (HAPDefinitionModule)defOrRef;
			HAPUtilityComponent.mergeWithParentAttachment(moduleDef, attachmentEx);
		}
		return HAPProcessorModule.process(moduleDef, id, this.m_runtimeEnv, this);
	}

	
	public HAPExecutableUIUnitPage getUIPage(HAPResourceId pageResourceId){
		HAPExecutableUIUnitPage out = (HAPExecutableUIUnitPage)this.m_resourceCache.getResource(pageResourceId);
		if(out==null) {
			out = getEmbededUIPage(pageResourceId, pageResourceId.getCoreIdLiterate(), null, null);
		}
		return out;
	}
	
	public HAPDefinitionUIPage getUIPageDefinition(HAPResourceId pageResourceId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionUIPage pageDefinition = (HAPDefinitionUIPage)this.m_runtimeEnv.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(pageResourceId, parentAttachment);
		return pageDefinition;
	}

	public HAPExecutableUIUnitPage getEmbededUIPage(HAPEntityOrReference defOrRef, String id, HAPContextProcessor processContext, HAPWithNameMapping withNameMapping){
		HAPDefinitionUIPage pageDef = null;
		HAPContainerAttachment attachmentEx = null;
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId pageId = (HAPResourceId)defOrRef;
			attachmentEx = HAPUtilityComponent.buildInternalAttachment(pageId, processContext==null?null:processContext.getAttachmentContainer(), withNameMapping);
			pageDef = getUIPageDefinition(pageId, attachmentEx);
		}
		
		//compile it
		HAPExecutableUIUnitPage out = HAPProcessorUIPage.processUIResource(pageDef, id, this.m_runtimeEnv, this, m_uiTagMan, this.m_uiResourceParser, m_idGengerator);
		return out;
	}
	
	public HAPExecutableUIUnitPage getUIPage(HAPDefinitionUIPage pageDef, String id) {
		HAPExecutableUIUnitPage out = HAPProcessorUIPage.processUIResource(pageDef, id, this.m_runtimeEnv, this, m_uiTagMan, this.m_uiResourceParser, m_idGengerator);
		return out;
	}

	public HAPExecutableUIUnitPage getUIPage(HAPDefinitionUIPage pageDef, String id, HAPContextProcessor processContext) {
		HAPExecutableUIUnitPage out = HAPProcessorUIPage.processUIResource(pageDef, id, this.m_runtimeEnv, this, m_uiTagMan, this.m_uiResourceParser, m_idGengerator);
		return out;
	}

	public HAPParserModule getModuleParser() {    return this.m_moduleParser;   }
	public HAPParserPage getUIResourceParser() {    return this.m_uiResourceParser;  }
	public HAPParseMiniApp getMinitAppParser() {    return this.m_miniAppParser;     }
	public HAPManagerUITag getUITagManager() {   return this.m_uiTagMan;   }
}
