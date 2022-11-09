package com.nosliw.data.core.runtime.js.browser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;

@HAPEntityWithAttribute
public class HAPGatewayBrowserLoadLibrary extends HAPGatewayImp{

	public static final String COMMAND_LOADLIBRARY = "loadLibrary";
	public static final String COMMAND_LOADLIBRARY_VERSION = "version";
	
	private HAPGatewayManager m_gatewayManager;
	
	public HAPGatewayBrowserLoadLibrary(HAPGatewayManager gatewayManager){
		this.m_gatewayManager = gatewayManager;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		
		switch(command){
		case COMMAND_LOADLIBRARY:
			HAPServiceData serviceData = this.m_gatewayManager.executeGateway(HAPRuntimeEnvironmentJS.GATEWAY_RESOURCE, HAPGatewayResource.COMMAND_DISCOVERANDLOADRESOURCES, parms, runtimeInfo);
			if(serviceData.isFail())   return serviceData;
			
			String version = parms.optString(COMMAND_LOADLIBRARY_VERSION);  //append version information for lib file
			List<String> fileNames = new ArrayList<String>();
			HAPGatewayOutput gatewayOutput = (HAPGatewayOutput)serviceData.getData();
			List<HAPJSScriptInfo> scripts = gatewayOutput.getScripts();
			for(HAPJSScriptInfo script : scripts){
				String file = script.isFile();
				if(file!=null){
					String scriptUrl = HAPRuntimeBrowserUtility.getBrowserScriptPath(file);
					String scriptUrlWithVersion = HAPUtilityBasic.addVersionToUrl(scriptUrl, version);
					fileNames.add(scriptUrlWithVersion);
				}
			}
			out = this.createSuccessWithObject(fileNames);
			break;
		}
		return out;
	}


}
