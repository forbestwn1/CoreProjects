package com.nosliw.core.gateway;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;

public abstract class HAPGatewayImp implements HAPGateway{

	
	protected HAPServiceData createSuccessWithScripts(List<HAPJSScriptInfo> scripts){
		return HAPServiceData.createSuccessData(new HAPGatewayOutput(scripts, null));
	}
	
	protected HAPServiceData createSuccessWithObject(Object data){
		return HAPServiceData.createSuccessData(new HAPGatewayOutput(null, data));
	}

	protected HAPServiceData createSuccess(List<HAPJSScriptInfo> scripts, Object data){
		return HAPServiceData.createSuccessData(new HAPGatewayOutput(scripts, data));
	}
	
	protected Object getSuccessData(HAPServiceData serviceData){
		HAPGatewayOutput output = (HAPGatewayOutput)serviceData.getData();
		return output.getData();
	}

	protected List<HAPJSScriptInfo> getSuccessScripts(HAPServiceData serviceData){
		HAPGatewayOutput output = (HAPGatewayOutput)serviceData.getData();
		return output.getScripts();
	}
}
