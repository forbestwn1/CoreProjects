package com.nosliw.data.core.domain.entity.service.provider;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityInDomainServiceProvider extends HAPManualEntity{

	public static final String ATTR_SERVICEKEY = "serviceKey";

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeObject(ATTR_SERVICEKEY, new HAPEmbededDefinition(serviceKey));	}

	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValue(ATTR_SERVICEKEY);	}
	
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
	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInDomainServiceProvider out = new HAPDefinitionEntityInDomainServiceProvider();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
