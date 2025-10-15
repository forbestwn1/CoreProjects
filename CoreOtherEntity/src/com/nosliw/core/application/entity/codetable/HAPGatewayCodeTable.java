package com.nosliw.core.application.entity.codetable;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
@Component
public class HAPGatewayCodeTable  extends HAPGatewayImp{

	@HAPAttribute
	static public final String COMMAND_GETCODETABLE = "getCodeTable";
	@HAPAttribute
	static public final String PARMS_GETCODETABLE_ID = "id";
	
	private HAPManagerCodeTable m_codeTableManager;
	
	public HAPGatewayCodeTable(HAPManagerCodeTable codeTableManager) {
		this.m_codeTableManager = codeTableManager;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		if(COMMAND_GETCODETABLE.equals(command)) {
			String id = parms.getString(PARMS_GETCODETABLE_ID);
			HAPCodeTable codeTable = this.m_codeTableManager.getCodeTable(new HAPCodeTableId(id));
			return this.createSuccessWithObject(codeTable);
		}
		return null;
	}

	@Override
	public String getName() {   return HAPConstantShared.GATEWAY_CODETABLE;  }

}
