package com.nosliw.data.core.service.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public class HAPInfoServiceStatic extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	private List<String> m_tags;
	
	private HAPInfoServiceInterface m_interface;
	
	public HAPInfoServiceStatic() {
		this.m_tags = new ArrayList<String>();
	}

	public HAPInfoServiceInterface getInterface() {  return this.m_interface;  } 
	
	public List<String> getTags(){   return this.m_tags;    }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {  
//		this.m_processedInterface = (HAPInfoServiceInterface)HAPUtilityResource.solidateResource(m_interface, runtimeEnv);
		this.m_interface.process(runtimeEnv);	
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			Object interfaceObj = objJson.get(INTERFACE);
			if(interfaceObj instanceof String) {
//				this.m_interface = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, interfaceObj);
			}
			else {
				HAPInfoServiceInterface serviceInterfaceInfo = new HAPInfoServiceInterface();
				serviceInterfaceInfo.buildObject(objJson, HAPSerializationFormat.JSON);
				this.m_interface = serviceInterfaceInfo;
			}

			this.m_tags.clear();
			JSONArray tagArray = objJson.optJSONArray(TAG);
			if(tagArray!=null) {
				for(int i=0; i<tagArray.length(); i++) {
					this.m_tags.add(tagArray.getString(i));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INTERFACE, this.m_interface.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TAG, HAPUtilityJson.buildJson(this.m_tags, HAPSerializationFormat.JSON));
//		if(this.m_interface.getEntityOrReferenceType().equals(HAPConstantShared.REFERENCE)) {
//			jsonMap.put(INTERFACE, ((HAPResourceId)this.m_interface).getCoreIdLiterate());
//			jsonMap.put(TAG, HAPUtilityJson.buildJson(this.m_tags, HAPSerializationFormat.JSON));
//		}
//		else {
//			HAPInfoServiceInterface serviceInterfaceInfo = this.m_interface;
//			serviceInterfaceInfo.buildJsonMap(jsonMap, typeJsonMap);
//		}
	}
}
