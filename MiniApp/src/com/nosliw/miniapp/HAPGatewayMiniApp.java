package com.nosliw.miniapp;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.miniapp.instance.HAPInstanceData;
import com.nosliw.miniapp.user.HAPUser;
import com.nosliw.miniapp.user.HAPUserInfo;
import com.nosliw.uiresource.application.HAPExecutableMiniAppEntry;

public class HAPGatewayMiniApp extends HAPGatewayImp{

	private HAPAppManager m_miniAppMan;
	
	@HAPAttribute
	public static final String COMMAND_LOGIN = "login";

	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP = "loadMiniApp";
	
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_APPID = "appId";
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_USERID = "userId";
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_ENTRY = "entry";

	@HAPAttribute
	public static final String COMMAND_CREATEDATA = "saveData";
	@HAPAttribute
	public static final String COMMAND_CREATEDATA_USERID = "userId";
	@HAPAttribute
	public static final String COMMAND_CREATEDATA_APPID = "appId";
	@HAPAttribute
	public static final String COMMAND_CREATEDATA_DATANAME = "dataName";
	@HAPAttribute
	public static final String COMMAND_CREATEDATA_DATAINFO = "dataInfo";

	@HAPAttribute
	public static final String COMMAND_UPDATEDATA = "updateData";
	@HAPAttribute
	public static final String COMMAND_UPDATEDATA_ID = "id";
	@HAPAttribute
	public static final String COMMAND_UPDATEDATA_DATAINFO = "dataInfo";

	@HAPAttribute
	public static final String COMMAND_DELETEDATA = "deleteData";
	@HAPAttribute
	public static final String COMMAND_DELETEDATA_DATATYPE = "dataType";
	@HAPAttribute
	public static final String COMMAND_DELETEDATA_ID = "id";
	
	public HAPGatewayMiniApp(HAPAppManager miniAppMan) {
		this.m_miniAppMan = miniAppMan;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) {
		
		HAPServiceData out = null;

		switch(command){
		case COMMAND_LOGIN:
		{
			String userId = null;
			if(parms!=null) {
				HAPUserInfo inUserInfo = ((HAPUserInfo)HAPSerializeManager.getInstance().buildObject(HAPUserInfo.class.getName(), parms, HAPSerializationFormat.JSON));
				HAPUser user = inUserInfo.getUser();
				if(user!=null) {
					userId = user.getId();
				}
			}
			
			HAPUserInfo userInfo = null;
			if(HAPBasicUtility.isStringEmpty(userId)) {
				userInfo = m_miniAppMan.createUser();
			}
			else {
				userInfo = m_miniAppMan.getUserInfo(userId);
				if(userInfo==null)  userInfo = m_miniAppMan.createUser();
			}
			out = HAPServiceData.createSuccessData(userInfo);
			break;
		}
		case COMMAND_LOADMINIAPP:
		{
			String appId = parms.optString(COMMAND_LOADMINIAPP_APPID);
			String userId = parms.optString(COMMAND_LOADMINIAPP_USERID);
			String appEntry = parms.optString(COMMAND_LOADMINIAPP_ENTRY);
			HAPExecutableMiniAppEntry miniAppInstance = m_miniAppMan.getMiniAppInstanceUIEntiry(userId, appId, appEntry);
			out = HAPServiceData.createSuccessData(miniAppInstance);
			break;
		}
		case COMMAND_CREATEDATA:
		{
			String userId = parms.optString(COMMAND_CREATEDATA_USERID);
			String appId = parms.optString(COMMAND_CREATEDATA_APPID);
			String dataName = parms.optString(COMMAND_CREATEDATA_DATANAME);
			
			JSONObject dataInfoJson = parms.optJSONObject(COMMAND_CREATEDATA_DATAINFO);
			HAPInstanceData dataInfo = HAPInstanceData.buildObject(dataInfoJson);
			HAPInstanceData newDataInfo = m_miniAppMan.createMiniAppData(userId, appId, dataName, dataInfo);
			out = HAPServiceData.createSuccessData(newDataInfo);
		}
		case COMMAND_DELETEDATA:
		{
			String id = parms.optString(COMMAND_DELETEDATA_ID);
			String dataType = parms.optString(COMMAND_DELETEDATA_DATATYPE);
			if(HAPBasicUtility.isStringEmpty(dataType))  dataType = HAPConstant.MINIAPPDATA_TYPE_SETTING; 
			m_miniAppMan.deleteMiniAppData(id, dataType);
			out = HAPServiceData.createSuccessData();
		}
		}
		
		return out;
	}

}
