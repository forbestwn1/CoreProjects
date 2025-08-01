package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPGatewayPackage extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE = "loadExecutablePackage";

	@HAPAttribute
	final public static String COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID = "resourceId";

	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPGatewayPackage(HAPManagerApplicationBrick brickManager) {
		this.m_brickManager = brickManager;
	}
	
	@Override
	public String getName() {  return HAPConstantShared.GATEWAY_PACKAGE;  }

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
		HAPApplicationPackage entityPackage = this.m_brickManager.getBrickPackage(resourceId);
		return this.createSuccessWithObject(entityPackage);
	}

}
