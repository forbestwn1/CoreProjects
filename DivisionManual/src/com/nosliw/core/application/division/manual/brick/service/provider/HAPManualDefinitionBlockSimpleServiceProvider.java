package com.nosliw.core.application.division.manual.brick.service.provider;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.service.provider.HAPKeyService;

public class HAPManualDefinitionBlockSimpleServiceProvider extends HAPManualDefinitionBrick{

	public static final String SERVICEKEY = "serviceKey";

	public HAPManualDefinitionBlockSimpleServiceProvider() {
		super(HAPEnumBrickType.SERVICEPROVIDER_100);
	}

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEKEY, serviceKey);	}

	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEKEY);	}
	
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
