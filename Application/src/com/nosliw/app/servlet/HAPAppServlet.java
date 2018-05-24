package com.nosliw.app.servlet;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.servlet.HAPServiceServlet;

@HAPEntityWithAttribute
public class HAPAppServlet extends HAPServiceServlet{

	private static final long serialVersionUID = 3449216679929442927L;

	@HAPAttribute
	public static final String COMMAND_LOGIN = "login";

	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP = "loadMiniApp";
	
	@HAPAttribute
	public static final String COMMAND_LOADMINIAPP_ID = "id";
	
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
			String miniAppId = parms.optString(COMMAND_LOADMINIAPP_ID);
			HAPMiniAppInstance miniAppInfo = miniAppMan.getMiniAppInstance(miniAppId);
			out = HAPServiceData.createSuccessData(miniAppInfo);
			break;
		}
		}
		
		return out;
	}
}
