package com.nosliw.data.core.service.provide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

//static information for a service. readable, query for service
@HAPEntityWithAttribute
public class HAPInfoServiceStatic extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";
	
	//service definition id
	private String m_id;
	
	private List<String> m_tags;
	
	private HAPServiceInterface m_serviceInterface;
	
	public HAPInfoServiceStatic() {
		this.m_tags = new ArrayList<String>();
		this.m_serviceInterface = new HAPServiceInterface();
	}

	public String getId() {   return this.m_id;  }
	
	public HAPServiceInterface getInterface() {  return this.m_serviceInterface;  } 
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			this.m_id = objJson.getString(ID);
			this.m_serviceInterface = new HAPServiceInterface();
			this.m_serviceInterface.buildObject(objJson.getJSONObject(INTERFACE), HAPSerializationFormat.JSON);
			this.m_tags.clear();
			this.m_tags.addAll(Arrays.asList(HAPNamingConversionUtility.parseElements(objJson.optString(TAG))));   
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
		jsonMap.put(ID, this.m_id);
		jsonMap.put(INTERFACE, HAPJsonUtility.buildJson(this.m_serviceInterface, HAPSerializationFormat.JSON));
		jsonMap.put(TAG, HAPJsonUtility.buildJson(this.m_tags, HAPSerializationFormat.JSON));
	}
}
