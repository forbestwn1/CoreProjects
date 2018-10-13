package com.nosliw.data.core.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDefinitionServiceInterface extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	@HAPAttribute
	public static String OUTPUT = "output";
	
	//service input parms
	private Map<String, HAPDefinitionServiceParm> m_parms;
	
	//service output
	private Map<String, HAPDefinitionServiceOutput> m_output;
	

	public HAPDefinitionServiceInterface() {
		this.m_parms = new LinkedHashMap<String, HAPDefinitionServiceParm>();
		this.m_output = new LinkedHashMap<String, HAPDefinitionServiceOutput>();
	}

	public Map<String, HAPDefinitionServiceParm> getParms(){  return this.m_parms;   }
	
	public Map<String, HAPDefinitionServiceOutput> getOutput(){ return this.m_output;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
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
