package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPManagerDomainEntityExecutable;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.imp.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.data.core.runtime.js.imp.browser.HAPGatewayLoadTestExpression;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeProcessRhinoImp;
import com.nosliw.data.core.script.expression1.HAPManagerScript;
import com.nosliw.data.core.service.definition.HAPGatewayService;
import com.nosliw.data.core.service.definition.HAPManagerService;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.imp.expression.parser.HAPDataExpressionParserImp;
import com.nosliw.ui.tag.HAPPluginResourceDefinitionUITagDefinition;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPGatewayUITag;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginApp;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginAppEntry;

public class HAPRuntimeEnvironmentImpBrowser extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_LOADLIBRARIES = "loadLibraries";

	@HAPAttribute
	public static final String GATEWAY_TESTEXPRESSION = "testExpression";
	
	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

	@HAPAttribute
	public static final String GATEWAY_UITAG = "uiTag";
	
	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	private HAPUIResourceManager m_uiResourceManager;
	
	public HAPRuntimeEnvironmentImpBrowser(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpBrowser(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPDataTypeManager dataTypeManager = new HAPDataTypeManagerImp(runtimeJSModule.getDataTypeDataAccess());
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPManagerCodeTable codeTableManager = new HAPManagerCodeTable();
		HAPResourceManagerJSImp resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPManagerDynamicResource dynamicResourceManager = new HAPManagerDynamicResource();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(dynamicResourceManager);
		HAPManagerService serviceManager = new HAPManagerService(this);
		HAPParserDataExpression dataExpressionParser = new HAPDataExpressionParserImp();
		HAPManagerScript scriptMan = new HAPManagerScript(this);
		HAPManagerTask taskManager = new HAPManagerTask(this);
		HAPManagerActivity activityMan = new HAPManagerActivity(new HAPManagerActivityPlugin(), this);
		HAPManagerProcess processMan = new HAPManagerProcess(this);
		HAPRuntimeProcess processRuntimeMan = new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = null;  //new HAPManagerCronJob(expressionMan, resourceMan, processMan, runtime, dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);
		HAPManagerStory storyManager = new HAPManagerStory(this); 
		HAPManagerDomainEntityExecutable complexEntityExecutableManager = new HAPManagerDomainEntityExecutable(this);
		HAPManagerDomainEntityDefinition domainEntityDefinitionManager = new HAPManagerDomainEntityDefinition();
		
		init(
			dataTypeManager,
			dataTypeHelper,
			codeTableManager,
			resourceMan,
			taskManager,
			activityMan,
			processMan,
			processRuntimeMan,
			dataExpressionParser,
			scriptMan,
			new HAPGatewayManager(),
			serviceManager,
			dynamicResourceManager,
			resourceDefManager,
			domainEntityDefinitionManager,
			complexEntityExecutableManager,
			cronJobManager,
			storyManager,
			runtime
		);

		this.m_uiResourceManager = new HAPUIResourceManager(this, new HAPManagerUITag(dataTypeHelper));

		//gateway
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
		this.getGatewayManager().registerGateway(GATEWAY_LOADLIBRARIES, new HAPGatewayBrowserLoadLibrary(this.getGatewayManager()));
		this.getGatewayManager().registerGateway(GATEWAY_TESTEXPRESSION, new HAPGatewayLoadTestExpression());
		this.getGatewayManager().registerGateway(GATEWAY_UITAG, new HAPGatewayUITag(this.m_uiResourceManager.getUITagManager()));

		//resource definition plugin
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpDefault(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, this.m_uiResourceManager.getUIResourceParser()));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpDefault(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE, this.m_uiResourceManager.getModuleParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginApp(this.m_uiResourceManager.getMinitAppParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginAppEntry(this.getResourceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionUITagDefinition(this));
		
		//story builder
//		this.getStoryManager().registerDesignDirector(HAPStoryBuilderPageSimple.BUILDERID, new HAPStoryBuilderPageSimple(this.m_uiResourceManager.getUITagManager(), this));
		
		//dynamic resource builder
//		this.getStoryManager().registerShowBuilder(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPBuilderPageSimple(this.getServiceManager().getServiceDefinitionManager(), this.getUIResourceManager().getUITagManager(), this.getUIResourceManager().getUIResourceParser()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }
}
