package com.nosliw.core.application.division.manual.brick.service.provider;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.service.provider.HAPKeyService;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;

public class HAPManualDefinitionBlockSimpleServiceProvider extends HAPManualDefinitionBrickBlockSimple{

	public static final String SERVICEKEY = "serviceKey";

	public HAPManualDefinitionBlockSimpleServiceProvider() {
		super(HAPEnumBrickType.SERVICEPROVIDER_100);
	}

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeWithValueValue(SERVICEKEY, serviceKey);	}

	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueWithValue(SERVICEKEY);	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		try{
			JSONObject objJson = (JSONObject)json;
			JSONObject serviceKeyJsonObj = objJson.getJSONObject(SERVICEKEY);
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
