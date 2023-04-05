package com.nosliw.servlet.core;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.user.HAPUser;
import com.nosliw.common.user.HAPUserInfo;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.user.HAPManagerUser;

public class HAPLoginServlet  extends HAPServiceServlet{
	private static final long serialVersionUID = 3449216679929442927L;

	@HAPAttribute
	public static final String COMMAND_LOGIN = "login";

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		HAPServiceData out = null;
		
		HAPAppManager miniAppMan = (HAPAppManager)this.getServletContext().getAttribute("minAppMan");

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
			if(HAPUtilityBasic.isStringEmpty(userId)) {
				userInfo = HAPManagerUser.getInstance().createUser();
			}
			else {
				userInfo = HAPManagerUser.getInstance().getUserInfo(userId);
				if(userInfo==null) {
					userInfo = HAPManagerUser.getInstance().createUser();
				}
				else {
					userInfo = miniAppMan.updateUserInfo(userInfo);
				}
			}
			out = HAPServiceData.createSuccessData(userInfo);
			break;
		}
		}
		return out;
	}
}
