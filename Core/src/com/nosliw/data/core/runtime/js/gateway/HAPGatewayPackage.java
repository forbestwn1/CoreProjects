package com.nosliw.data.core.runtime.js.gateway;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.domain.HAPPackageExecutable;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayPackage extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE = "loadExecutablePackage";

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID = "resourceId";

	private HAPRuntimeEnvironment m_runtimeEnviroment;
	
	public HAPGatewayPackage(HAPRuntimeEnvironment runtimeEnviroment) {
		this.m_runtimeEnviroment = runtimeEnviroment;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		try{
			switch(command){
			case COMMAND_LOADEXECUTABLEPACKAGE:
				out = this.requestLoadExecutablePackage(parms, runtimeInfo);
				break;
			}
		}
		catch(Exception e){
			out = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		return out;
	}

	private HAPServiceData requestLoadExecutablePackage(JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception{
		Object idObj = parms.get(COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID);
		HAPResourceId resourceId = HAPFactoryResourceId.newInstance(idObj);
		HAPPackageExecutable executablePackage =	this.m_runtimeEnviroment.getComplexEntityManager().getExecutablePackage(resourceId);
		return this.createSuccessWithObject(executablePackage);
	}
}
