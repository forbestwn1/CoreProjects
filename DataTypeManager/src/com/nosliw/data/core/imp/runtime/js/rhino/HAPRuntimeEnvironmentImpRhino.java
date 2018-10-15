package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.service1.HAPGatewayService;
import com.nosliw.data.core.service1.HAPManagerService;
import com.nosliw.data.core.task.HAPManagerTask;
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
		
		HAPResourceManagerRoot resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPRuntimeImpRhino runtime = new HAPRuntimeImpRhino(this); 
		HAPGatewayManager gatewayManager = new HAPGatewayManager(); 
		HAPExpressionSuiteManager expressionManager = new HAPExpressionSuiteManager(); 		
		HAPManagerTask taskManager = new HAPManagerTask(runtime);
		HAPManagerService serviceManager = new HAPManagerService();

		init(resourceMan,
			taskManager,
			expressionManager,
			gatewayManager,
			serviceManager,
			runtime
		);

		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
	}

}
