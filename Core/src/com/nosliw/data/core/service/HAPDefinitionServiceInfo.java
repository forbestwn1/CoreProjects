package com.nosliw.data.core.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDefinitionServiceInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";
	
	//service definition id
	private String m_id;
	
	//service name, for display
	private String m_name;

	//service description
	private String m_description;
	
	private List<String> m_tags;
	
	private HAPDefinitionServiceInterface m_serviceInterface;
	
	public HAPDefinitionServiceInfo() {
		this.m_tags = new ArrayList<String>();
	}

	public String getId() {   return this.m_id;  }
	
	public String getName(){  return this.m_name;   }
	
	public String getDescription(){   return this.m_description;   }
	
	public HAPDefinitionServiceInterface getInterface() {  return this.m_serviceInterface;  } 
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.getString(NAME);
			this.m_description = objJson.optString(DESCRIPTION);
			
			this.m_serviceInterface = new HAPDefinitionServiceInterface();
			this.m_serviceInterface.buildObject(objJson.getJSONObject(INTERFACE), HAPSerializationFormat.JSON);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
