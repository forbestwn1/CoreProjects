package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeEnvironmentImpJS extends HAPRuntimeEnvironmentJS{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeEnvironmentImpJS(HAPModuleRuntimeJS runtimeJSModule) {
		super(new HAPResourceDiscoveryJSImp(
				runtimeJSModule.getRuntimeJSDataAccess()), 
				new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess()),
				new HAPExpressionManagerImp(new HAPExpressionParserImp(), new HAPDataTypeHelperImp(runtimeJSModule.getDataTypeDataAccess()))
				);

		this.m_runtimeJSModule = runtimeJSModule;
	}
}
