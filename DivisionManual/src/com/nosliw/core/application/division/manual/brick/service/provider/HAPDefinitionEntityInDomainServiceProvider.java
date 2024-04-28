package com.nosliw.core.application.division.manual.brick.service.provider;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.HAPManualBrick;

public class HAPDefinitionEntityInDomainServiceProvider extends HAPManualBrick{

	public static final String ATTR_SERVICEKEY = "serviceKey";

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeWithValueValue(ATTR_SERVICEKEY, serviceKey);	}

	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueWithValue(ATTR_SERVICEKEY);	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		try{
			JSONObject objJson = (JSONObject)json;
			JSONObject serviceKeyJsonObj = objJson.getJSONObject(ATTR_SERVICEKEY);
			HAPKeyService serviceKey = new HAPKeyService();
			serviceKey.buildObject(serviceKeyJsonObj, HAPSerializationFormat.JSON);
			this.setServiceKey(serviceKey);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
	
}
