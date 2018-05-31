package com.nosliw.app.servlet;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.miniapp.instance.HAPInstanceMiniAppUIEntry;
import com.nosliw.miniapp.user.HAPUser;
import com.nosliw.miniapp.user.HAPUserInfo;
import com.nosliw.servlet.HAPServiceServlet;

@HAPEntityWithAttribute
public class HAPAppServlet extends HAPServiceServlet{

	private static final long serialVersionUID = 3449216679929442927L;

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
	public static final String COMMAND_SAVEDATA = "saveData";
	@HAPAttribute
	public static final String COMMAND_SAVEDATA_USERID = "userId";
	@HAPAttribute
	public static final String COMMAND_SAVEDATA_APPID = "appId";
	@HAPAttribute
	public static final String COMMAND_SAVEDATA_DATANAME = "dataName";
	@HAPAttribute
	public static final String COMMAND_SAVEDATA_DATA = "data";

	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		
		HAPAppManager miniAppMan = (HAPAppManager)this.getServletContext().getAttribute("minAppMan");
		
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
				userInfo = miniAppMan.createUser();
			}
			else {
				userInfo = miniAppMan.getUserInfo(userId);
				if(userInfo==null)  userInfo = miniAppMan.createUser();
			}
			out = HAPServiceData.createSuccessData(userInfo);
			break;
		}
		case COMMAND_LOADMINIAPP:
		{
			String appId = parms.optString(COMMAND_LOADMINIAPP_APPID);
			String userId = parms.optString(COMMAND_LOADMINIAPP_USERID);
			String appEntry = parms.optString(COMMAND_LOADMINIAPP_ENTRY);
			HAPInstanceMiniAppUIEntry miniAppInstance = miniAppMan.getMiniAppInstanceUIEntiry(userId, appId, appEntry);
			out = HAPServiceData.createSuccessData(miniAppInstance);
			break;
		}
		case COMMAND_SAVEDATA:
		{
			
		}
		}
		
		return out;
	}
}
