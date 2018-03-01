package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.HAPGatewayDataSource;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.datasource.HAPDataSourceManagerImp;
import com.nosliw.data.core.imp.expression.HAPExpressionTaskManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.task.HAPTaskManager;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeEnvironmentImpRhino extends HAPRuntimeEnvironmentJS{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	HAPDataTypeHelperImp m_dataTypeHelper;
	
	public HAPRuntimeEnvironmentImpRhino(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpRhino(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		this.m_dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		
		HAPResourceManagerRoot resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPTaskManager expressionManager = new HAPExpressionTaskManagerImp(new HAPExpressionParserImp(), this.m_dataTypeHelper); 		
		HAPRuntimeImpRhino runtime = new HAPRuntimeImpRhino(this); 
		HAPGatewayManager gatewayManager = new HAPGatewayManager(); 

		init(resourceMan,
			expressionManager,
			gatewayManager,
			runtime
		);

		//data source gateway
		this.getGatewayManager().registerGateway(HAPDataSourceManager.GATEWAY_DATASOURCE, new HAPGatewayDataSource(new HAPDataSourceManagerImp(this.getExpressionManager(), this.getRuntime())));
	}

	public HAPDataTypeHelper getDataTypeHelper(){
		return this.m_dataTypeHelper;
	}
	
}
