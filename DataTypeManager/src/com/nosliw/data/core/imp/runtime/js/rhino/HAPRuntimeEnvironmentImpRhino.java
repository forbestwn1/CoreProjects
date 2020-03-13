package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionSuiteManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeProcessRhinoImp;
import com.nosliw.data.core.service.provide.HAPGatewayService;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.template.HAPManagerTemplate;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeEnvironmentImpRhino extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeEnvironmentImpRhino(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpRhino(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPExpressionManager.dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPExpressionManager.expressionParser = new HAPExpressionParserImp();
		
		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPResourceManagerJSImp resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPManagerTemplate templateManager = new HAPManagerTemplate();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(templateManager);
		HAPManagerService serviceManager = new HAPManagerService();
		HAPExpressionSuiteManager expSuiteMan = new HAPExpressionSuiteManager();
		HAPManagerProcess processMan = new HAPManagerProcess(new HAPManagerActivityPlugin(), resourceDefManager, HAPExpressionManager.dataTypeHelper, runtime, expSuiteMan, serviceManager.getServiceDefinitionManager());
		HAPRuntimeProcess processRuntimeMan = new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = new HAPManagerCronJob(expSuiteMan, resourceMan, processMan, runtime, HAPExpressionManager.dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);

		init(resourceMan,
				processMan,
				processRuntimeMan,
				expSuiteMan,
				new HAPGatewayManager(),
				serviceManager,
				templateManager,
				resourceDefManager,
				cronJobManager,
				runtime
		);

		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
	}
}
