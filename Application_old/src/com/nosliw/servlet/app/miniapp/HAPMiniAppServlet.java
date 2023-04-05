package com.nosliw.servlet.app.miniapp;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.servlet.HAPServiceServlet;

@HAPEntityWithAttribute
public class HAPMiniAppServlet extends HAPServiceServlet{
	private static final long serialVersionUID = 3449216679929442927L;

	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP = "loadMiniApp";
	
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_APPID = "appId";
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_USERID = "userId";
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_ENTRY = "entry";

	@HAPAttribute
	public static final String COMMAND_CREATEDATA = "createData";
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
	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		HAPServiceData out = null;
		
		HAPAppManager miniAppMan = (HAPAppManager)this.getServletContext().getAttribute("minAppMan");

		switch(command){
		case COMMAND_LOADMINIAPP:
		{
//			String appId = parms.optString(COMMAND_LOADMINIAPP_APPID);
//			String userId = parms.optString(COMMAND_LOADMINIAPP_USERID);
//			String appEntry = parms.optString(COMMAND_LOADMINIAPP_ENTRY);
//			HAPMiniAppEntryInstance miniAppInstance = miniAppMan.getMiniAppInstanceUIEntry(userId, appId, appEntry);
//			out = HAPServiceData.createSuccessData(miniAppInstance);
			break;
		}
		case COMMAND_CREATEDATA:
		{
//			String userId = parms.optString(COMMAND_CREATEDATA_USERID);
//			String appId = parms.optString(COMMAND_CREATEDATA_APPID);
//			String dataName = parms.optString(COMMAND_CREATEDATA_DATANAME);
//			
//			JSONObject dataInfoJson = parms.optJSONObject(COMMAND_CREATEDATA_DATAINFO);
//			HAPSettingData dataInfo = HAPSettingData.buildObject(dataInfoJson);
//			HAPSettingData newDataInfo = miniAppMan.createMiniAppData(userId, appId, dataName, dataInfo);
//			out = HAPServiceData.createSuccessData(newDataInfo);
			break;
		}
		case COMMAND_UPDATEDATA:
		{
//			String id = parms.optString(COMMAND_UPDATEDATA_ID);
//			JSONObject dataInfoJson = parms.optJSONObject(COMMAND_UPDATEDATA_DATAINFO);
//			HAPSettingData dataInfo = HAPSettingData.buildObject(dataInfoJson);
//			HAPSettingData newDataInfo = miniAppMan.updateMiniAppData(id, dataInfo);
//			out = HAPServiceData.createSuccessData(newDataInfo);
			break;
		}
		case COMMAND_DELETEDATA:
		{
//			String id = parms.optString(COMMAND_DELETEDATA_ID);
//			String dataType = parms.optString(COMMAND_DELETEDATA_DATATYPE);
//			if(HAPBasicUtility.isStringEmpty(dataType))  dataType = HAPConstant.MINIAPPDATA_TYPE_SETTING; 
//			miniAppMan.deleteMiniAppData(id, dataType);
//			out = HAPServiceData.createSuccessData();
			break;
		}
		}
		return out;
	}
}
