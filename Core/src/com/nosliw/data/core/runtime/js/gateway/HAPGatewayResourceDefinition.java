package com.nosliw.data.core.runtime.js.gateway;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayResourceDefinition extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_LOADRESOURCEDEFINITION = "requestLoadResourceDefinition";
	@HAPAttribute
	final public static String COMMAND_LOADRESOURCEDEFINITION_ID = "resourceId";
	
	private HAPRuntimeEnvironment m_runtimeEnviroment;
	
	public HAPGatewayResourceDefinition(HAPRuntimeEnvironment runtimeEnviroment){
		this.m_runtimeEnviroment = runtimeEnviroment;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		HAPServiceData out = null;
		try{
			switch(command){
			case COMMAND_LOADRESOURCEDEFINITION:
				out = this.requestLoadResourceDefinition(parms, runtimeInfo);
				break;
			}
		}
		catch(Exception e){
			out = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		return out;
	}

	private HAPServiceData requestLoadResourceDefinition(JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception{
		Object idObj = parms.get(COMMAND_LOADRESOURCEDEFINITION_ID);
		HAPResourceId resourceId = HAPFactoryResourceId.newInstance(idObj);
		HAPResourceDefinition1 resourceDefinition = this.m_runtimeEnviroment.getResourceDefinitionManager().getResourceDefinition(resourceId);
		return this.createSuccessWithObject(resourceDefinition);
	}
}
