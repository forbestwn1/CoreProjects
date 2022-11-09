package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

@HAPEntityWithAttribute
public class HAPDefinitionServiceProvider extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SERVICEID = "serviceId";

	@HAPAttribute
	public static String SERVICEINTERFACE = "serviceInterface";

	private String m_serviceId;
	
	private HAPServiceInterface m_serviceInterface;
	
	public HAPDefinitionServiceProvider() {}
	
	public String getServiceId() {    return this.m_serviceId;    }
	public void setServiceId(String id) {    this.m_serviceId = id;    }
	
	public HAPServiceInterface getServiceInterface() {  return this.m_serviceInterface;  }
	public void setServiceInterface(HAPServiceInterface serviceInterface) {   this.m_serviceInterface = serviceInterface;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICEID, this.m_serviceId);
		jsonMap.put(SERVICEINTERFACE, HAPUtilityJson.buildJson(this.m_serviceInterface, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject interfaceJsonObj = jsonObj.optJSONObject(SERVICEINTERFACE);
		if(interfaceJsonObj!=null) {
			this.m_serviceInterface = new HAPServiceInterface();
			this.m_serviceInterface.buildObject(interfaceJsonObj, HAPSerializationFormat.JSON);
		}
		this.m_serviceId = (String)jsonObj.opt(SERVICEID);
		return true;  
	}

	@Override
	public HAPDefinitionServiceProvider clone() {
		HAPDefinitionServiceProvider out = new HAPDefinitionServiceProvider();
		this.cloneToEntityInfo(out);
		out.m_serviceId = this.m_serviceId;
		out.m_serviceInterface = this.m_serviceInterface;
		return out;
	}
	
}
