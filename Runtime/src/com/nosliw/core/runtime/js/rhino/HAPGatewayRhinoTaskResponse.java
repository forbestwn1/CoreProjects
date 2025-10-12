package com.nosliw.core.runtime.js.rhino;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPGatewayRhinoTaskResponse extends HAPGatewayImp{

	public static final String COMMAND_SUCCESS = "success";
	public static final String COMMAND_ERROR = "error";
	public static final String COMMAND_EXCEPTION = "exception";

	public static final String PARM_TASKID = "taskId";
	public static final String PARM_RESPONSEDATA = "responseData";
	
	private HAPRuntimeImpRhino m_runtime;
	
	public HAPGatewayRhinoTaskResponse(HAPRuntimeImpRhino runtime){
		this.m_runtime = runtime;
	}
	
	@Override
	public String getName() {	return HAPConstantShared.GATEWAY_RHINOTASKRESPONSE;	}

	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception{
		HAPServiceData serviceData = null;
		String taskId = parms.getString(PARM_TASKID);
		Object responseData = parms.opt(PARM_RESPONSEDATA);
		switch(command){
		case COMMAND_SUCCESS:
			serviceData = HAPServiceData.createSuccessData(responseData);
			break;
		case COMMAND_ERROR:
		case COMMAND_EXCEPTION:
			serviceData = new HAPServiceData();
			serviceData.buildObject(responseData, HAPSerializationFormat.JSON);
			break;
		}
		this.m_runtime.finishTask(taskId, serviceData);
		return this.createSuccessWithObject(null);
	}


}
