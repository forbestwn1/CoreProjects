package com.nosliw.miniapp;

import com.nosliw.common.user.HAPUserInfo;
import com.nosliw.miniapp.entity.HAPUserMiniAppInfo;

public class HAPUtility {

	private static String ENTITY_MINIAPPINFO = "ENTITY_MINIAPPINFO";
	
	public static HAPUserMiniAppInfo getUserMiniAppInfo(HAPUserInfo userInfo) {
		return (HAPUserMiniAppInfo)userInfo.getRelatedEntity(ENTITY_MINIAPPINFO);
	}
	
	public static void setUserMiniAppInfo(HAPUserInfo userInfo, HAPUserMiniAppInfo userMiniAppInfo) {
		userInfo.setRelatedEntity(ENTITY_MINIAPPINFO, userMiniAppInfo);
	}
	
}
