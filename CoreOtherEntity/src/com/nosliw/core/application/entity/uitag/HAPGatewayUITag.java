package com.nosliw.core.application.entity.uitag;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
@Component
public class HAPGatewayUITag extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETDEFAULTTAG = "getDefaultTag";
	@HAPAttribute
	final public static String COMMAND_GETDEFAULTTAG_CRITERIA = "criteria";

	@HAPAttribute
	final public static String COMMAND_QUERYTAG = "queryTag";
	@HAPAttribute
	final public static String COMMAND_QUERYTAG_CRITERIA = "criteria";

	private HAPManagerUITag m_uiTagMan;
	
	public HAPGatewayUITag(HAPManagerUITag uiTagMan) {
		this.m_uiTagMan = uiTagMan;
	}
	
	@Override
	public String getName() {   return HAPConstantShared.GATEWAY_UITAG;    }

	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		HAPServiceData out = null;
		try{
			switch(command){
			case COMMAND_GETDEFAULTTAG:
			{
				HAPUITageQueryData query = new HAPUITageQueryData(HAPUtilityCriteria.parseCriteria(parms.getString(COMMAND_GETDEFAULTTAG_CRITERIA)));
				HAPUITagInfo result = this.m_uiTagMan.getDefaultUITagData(query);
				out = this.createSuccessWithObject(result);
				break;
			}
			case COMMAND_QUERYTAG:
			{
				HAPUITageQueryData query = new HAPUITageQueryData(HAPUtilityCriteria.parseCriteria(parms.getString(COMMAND_QUERYTAG_CRITERIA)));
				HAPUITagQueryResultSet result = this.m_uiTagMan.queryUITagData(query);
				out = this.createSuccessWithObject(result);
				break;
			}
			}
		}
		catch(Exception e){
			out = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		return out;
	}

}
