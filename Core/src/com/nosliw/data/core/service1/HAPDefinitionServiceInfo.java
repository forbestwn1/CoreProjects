package com.nosliw.data.core.service1;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionServiceInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String PARM = "parm";
	
	@HAPAttribute
	public static String OUTPUT = "output";
	
	//service definition id
	private String m_id;
	
	//service name
	private String m_name;

	//service description
	private String m_description;
	
	//service input parms
	private Map<String, HAPDefinitionServiceParm> m_parms;
	
	//service output
	private Map<String, HAPDefinitionServiceOutput> m_output;
	
	public HAPDefinitionServiceInfo() {
		this.m_parms = new LinkedHashMap<String, HAPDefinitionServiceParm>();
		this.m_output = new LinkedHashMap<String, HAPDefinitionServiceOutput>();
	}

	public String getId() {   return this.m_id;  }
	
	public String getName(){  return this.m_name;   }
	
	public String getDescription(){   return this.m_description;   }
	
	public Map<String, HAPDefinitionServiceParm> getParms(){  return this.m_parms;   }
	
	public Map<String, HAPDefinitionServiceOutput> getOutput(){ return this.m_output;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.getString(NAME);
			this.m_description = objJson.optString(DESCRIPTION);

			JSONArray outputArray = objJson.getJSONArray(OUTPUT);
			for(int i = 0; i<outputArray.length(); i++){
				HAPDefinitionServiceOutput outputEle = new HAPDefinitionServiceOutput();
				outputEle.buildObject(outputArray.get(i), HAPSerializationFormat.JSON);
				this.m_output.put(outputEle.getName(), outputEle);
			}
			
			
			JSONArray parmsArray = objJson.getJSONArray(PARM);
			for(int i = 0; i<parmsArray.length(); i++){
				HAPDefinitionServiceParm parm = new HAPDefinitionServiceParm();
				parm.buildObject(parmsArray.get(i), HAPSerializationFormat.JSON);
				this.m_parms.put(parm.getName(), parm);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
