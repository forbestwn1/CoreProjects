package com.nosliw.app.utils;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;

/*
 * create error service data when cannot find client id on server side
 */
public class HAPApplicationErrorUtility {

	public static HAPServiceData createClientIdInvalidError(String clientId){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_APPLICATION_INVALIDCLIENTID, clientId, "client id " + clientId + "does not exist.");
		return out;
	}
	
	
}
