package com.nosliw.miniapp;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;
import com.nosliw.miniapp.data.HAPAppDataManagerImp;
import com.nosliw.miniapp.entity.HAPOwnerInfo;

@HAPEntityWithAttribute
public class HAPGatewayAppData extends HAPGatewayImp{

	@HAPAttribute
	final public static String GATEWAY_APPDATA = "appData";

	@HAPAttribute
	final public static String COMMAND_GETOWNERAPPDATA = "getOwnerAppData";
	@HAPAttribute
	final public static String COMMAND_GETOWNERAPPDATA_OWNER = "owner";

	@HAPAttribute
	final public static String COMMAND_GETAPPDATA = "getAppData";
	@HAPAttribute
	final public static String COMMAND_GETAPPDATA_INFOS = "infos";

	@HAPAttribute
	final public static String COMMAND_UPDATEAPPDATA = "updateAppData";
	@HAPAttribute
	final public static String COMMAND_UPDATEAPPDATA_INFOS = "infos";
	
	private HAPAppDataManagerImp m_appDataManager;

	public HAPGatewayAppData(HAPAppDataManagerImp appDataManager) {
		this.m_appDataManager = appDataManager;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		try{
			switch(command){
			case COMMAND_GETOWNERAPPDATA:
				out = this.getOwnerAppData(parms, runtimeInfo);
				break;
			case COMMAND_GETAPPDATA:
				out = this.getAppData(parms, runtimeInfo);
				break;
			case COMMAND_UPDATEAPPDATA:
				out = this.updateAppData(parms, runtimeInfo);
				break;
			}
		}
		catch(Exception e){
			out = HAPServiceData.createFailureData(e, "");
			e.printStackTrace();
		}
		return out;
	}

	private HAPServiceData getOwnerAppData(JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		JSONObject ownerJson = parms.getJSONObject(COMMAND_GETOWNERAPPDATA_OWNER);
		HAPOwnerInfo owner = new HAPOwnerInfo();
		owner.buildObject(ownerJson, HAPSerializationFormat.JSON);
		HAPAppDataInfoContainer infos = this.m_appDataManager.getAppData(owner);
		return this.createSuccessWithObject(infos);
	}


	private HAPServiceData getAppData(JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		JSONObject infosJson = parms.getJSONObject(COMMAND_GETAPPDATA_INFOS);
		HAPAppDataInfoContainer infos = new HAPAppDataInfoContainer();
		infos.buildObject(infosJson, HAPSerializationFormat.JSON);
		this.m_appDataManager.getAppData(infos);
		return this.createSuccessWithObject(infos);
	}

	
	private HAPServiceData updateAppData(JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		JSONObject infosJson = parms.getJSONObject(COMMAND_UPDATEAPPDATA_INFOS);
		HAPAppDataInfoContainer infos = new HAPAppDataInfoContainer();
		infos.buildObject(infosJson, HAPSerializationFormat.JSON);
		this.m_appDataManager.getAppData(infos);
		return this.createSuccessWithObject(infos);
	}

}
