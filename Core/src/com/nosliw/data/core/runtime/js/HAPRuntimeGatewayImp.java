package com.nosliw.data.core.runtime.js;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;

public abstract class HAPRuntimeGatewayImp implements HAPRuntimeGateway{

	
	protected HAPServiceData createSuccessWithScripts(List<HAPJSScriptInfo> scripts){
		return HAPServiceData.createSuccessData(new HAPRuntimeGatewayOutput(scripts, null));
	}
	
	protected HAPServiceData createSuccessWithObject(Object data){
		return HAPServiceData.createSuccessData(new HAPRuntimeGatewayOutput(null, data));
	}

	protected HAPServiceData createSuccess(List<HAPJSScriptInfo> scripts, Object data){
		return HAPServiceData.createSuccessData(new HAPRuntimeGatewayOutput(scripts, data));
	}
	
	protected Object getSuccessData(HAPServiceData serviceData){
		HAPRuntimeGatewayOutput output = (HAPRuntimeGatewayOutput)serviceData.getData();
		return output.getData();
	}

	protected List<HAPJSScriptInfo> getSuccessScripts(HAPServiceData serviceData){
		HAPRuntimeGatewayOutput output = (HAPRuntimeGatewayOutput)serviceData.getData();
		return output.getScripts();
	}
}
