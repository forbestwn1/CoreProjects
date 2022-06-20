package com.nosliw.data.core.runtime.js.gateway;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.domain.HAPPackageExecutable;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;

public class HAPGatewayPackage extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE = "loadExecutablePackage";

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE_ID = "resourceId";

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
		Object idObj = parms.get(COMMAND_LOADEXECUTABLEPACKAGE_ID);
		HAPResourceId resourceId = HAPFactoryResourceId.newInstance(idObj);
		HAPPackageExecutable executablePackage =	this.m_runtimeEnviroment.getComplexEntityManager().getExecutablePackage(resourceId);
		
		
		
		List<HAPJSScriptInfo> scriptsInfo = new ArrayList<HAPJSScriptInfo>();
		
		HAPUtilityRuntimeJSScript.buildScriptForResource(resourceInfo, resource)
		
		for(HAPResource resource : loadResourceResponse.getLoadedResources()){
			HAPResourceInfo resourceInfo = resourcesInfo.get(resource.getId());
			scriptsInfo.addAll(HAPUtilityRuntimeJSScript.buildScriptForResource(resourceInfo, resource));
		}
		serviceData = this.createSuccessWithScripts(scriptsInfo); 

		
		HAPResourceDefinition1 resourceDefinition = this.m_runtimeEnviroment.getResourceDefinitionManager().getLocalResourceDefinition(resourceId);
		return this.createSuccessWithObject(resourceDefinition);
	}

}
