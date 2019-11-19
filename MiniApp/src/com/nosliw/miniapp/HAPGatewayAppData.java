package com.nosliw.miniapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.miniapp.data.HAPAppDataManagerImp;
import com.nosliw.miniapp.data.HAPMiniAppSettingData;
import com.nosliw.miniapp.data.HAPSettingData;
import com.nosliw.miniapp.entity.HAPOwnerInfo;

@HAPEntityWithAttribute
public class HAPGatewayAppData extends HAPGatewayImp{

	@HAPAttribute
	final public static String GATEWAY_APPDATA = "appData";
	
	@HAPAttribute
	final public static String COMMAND_GETAPPDATA = "getAppData";
	@HAPAttribute
	final public static String COMMAND_GETAPPDATA_OWNER = "owner";
	@HAPAttribute
	final public static String COMMAND_GETAPPDATA_DATANAME = "dataName";

	@HAPAttribute
	final public static String COMMAND_UPDATEAPPDATA = "updateAppData";
	@HAPAttribute
	final public static String COMMAND_UPDATEAPPDATA_OWNER = "owner";
	@HAPAttribute
	final public static String COMMAND_UPDATEAPPDATA_DATABYNAME = "dataByName";
	
	private HAPAppDataManagerImp m_appDataManager;

	public HAPGatewayAppData(HAPAppDataManagerImp appDataManager) {
		this.m_appDataManager = appDataManager;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		try{
			switch(command){
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

	private HAPServiceData getAppData(JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		HAPMiniAppSettingData out = null;
		
		JSONObject ownerJson = parms.getJSONObject(COMMAND_GETAPPDATA_OWNER);
		HAPOwnerInfo ownerInfo = new HAPOwnerInfo();
		ownerInfo.buildObject(ownerJson, HAPSerializationFormat.JSON);
		
		JSONArray dataNameArray = parms.optJSONArray(COMMAND_GETAPPDATA_DATANAME);
		if(dataNameArray==null || dataNameArray.length()==0) {
			out = this.m_appDataManager.getAppData(ownerInfo);
		}
		else {
			List<String> dataNames = new ArrayList<String>();
			for(int i=0; i<dataNameArray.length(); i++) {
				dataNames.add(dataNameArray.getString(i));
			}
			out = this.m_appDataManager.getAppData(ownerInfo, dataNames.toArray(new String[0]));
		}
		return this.createSuccessWithObject(out);
	}
	
	private HAPServiceData updateAppData(JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		HAPMiniAppSettingData out = null;
		
		JSONObject ownerJson = parms.getJSONObject(COMMAND_UPDATEAPPDATA_OWNER);
		HAPOwnerInfo ownerInfo = new HAPOwnerInfo();
		ownerInfo.buildObject(ownerJson, HAPSerializationFormat.JSON);

		out = new HAPMiniAppSettingData(ownerInfo);
		
		JSONObject dataByNameJson = parms.getJSONObject(COMMAND_UPDATEAPPDATA_DATABYNAME);
		for(Object key : dataByNameJson.keySet()) {
			String dataName = (String)key;
			HAPSettingData settingData = new HAPSettingData();
			settingData.buildObject(dataByNameJson.getJSONObject(dataName), HAPSerializationFormat.JSON);
			out.addData(settingData);
		}
		
		out = this.m_appDataManager.updateAppData(out);
		return this.createSuccessWithObject(out);
	}
}
