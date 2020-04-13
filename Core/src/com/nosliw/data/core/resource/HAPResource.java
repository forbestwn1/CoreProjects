package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

/**
 * This interface represent a resource 
 * Every resource have 
 * 		a global unique id
 *  	type of resource
 *  	status of resource: available, not available
 */
@HAPEntityWithAttribute
public class HAPResource extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String RESOURCEDATA = "resourceData";

	@HAPAttribute
	public static String INFO = "info";
	
	private HAPResourceId m_id;
	
	private HAPResourceData m_resourceData;
	
	private HAPInfo m_info;

	public HAPResource(HAPResourceId id, HAPResourceData resourceData, Map<String, Object> infoValues){
		this.m_id = id;
		this.m_resourceData = resourceData;
		this.m_info = new HAPInfoImpSimple();
		if(infoValues!=null){
			for(String name : infoValues.keySet()){
				this.m_info.setValue(name, infoValues.get(name));
			}
		}
	}
	
	public HAPResourceId getId(){		return this.m_id;	}
	
	public Object getInfoValue(String name){ return this.m_info.getValue(name);  }
	
	public HAPInfo getInfo(){  return this.m_info;  }
	
	public HAPResourceData getResourceData(){  return this.m_resourceData;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, HAPSerializeManager.getInstance().toStringValue(this.m_id, HAPSerializationFormat.JSON));
		jsonMap.put(RESOURCEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_resourceData, HAPSerializationFormat.JSON_FULL));
		jsonMap.put(INFO, HAPSerializeManager.getInstance().toStringValue(this.m_info, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, HAPSerializeManager.getInstance().toStringValue(this.m_id, HAPSerializationFormat.JSON));
		jsonMap.put(RESOURCEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_resourceData, HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPSerializeManager.getInstance().toStringValue(this.m_info, HAPSerializationFormat.JSON));
	}
	
	@Override
	public String toString() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		jsonMap.put(ID, HAPSerializeManager.getInstance().toStringValue(this.m_id, HAPSerializationFormat.JSON));
		if(this.m_resourceData instanceof HAPResourceDataJSValue)  jsonMap.put(RESOURCEDATA, ((HAPResourceDataJSValue)this.m_resourceData).getValue());
		else jsonMap.put(RESOURCEDATA, this.m_resourceData.toString());
		typeJsonMap.put(RESOURCEDATA, HAPJsonTypeScript.class);
		jsonMap.put(INFO, HAPSerializeManager.getInstance().toStringValue(this.m_info, HAPSerializationFormat.JSON));
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}
}
