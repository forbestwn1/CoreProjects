package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPRuntimeEnvironmentImpJS extends HAPRuntimeEnvironmentJS{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	HAPDataTypeHelperImp m_dataTypeHelper;
	
	public HAPRuntimeEnvironmentImpJS(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpJS(HAPModuleRuntimeJS runtimeJSModule) {
		super(new HAPResourceDiscoveryJSImp(runtimeJSModule.getRuntimeJSDataAccess()), 
				new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess()),
				new HAPExpressionManagerImp(new HAPExpressionParserImp(), new HAPDataTypeHelperImp(runtimeJSModule.getDataTypeDataAccess()))
				);

		this.m_runtimeJSModule = runtimeJSModule;
	}
	
	private HAPDataTypeHelperImp getDataTypeHelper(){
		if(this.m_dataTypeHelper==null){
			this.m_dataTypeHelper = new HAPDataTypeHelperImp();
		}
		return this.m_dataTypeHelper;
	}
}
