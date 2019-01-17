package com.nosliw.data.core.service.interfacee;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPServiceInterface extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	//service input parms
	private Map<String, HAPServiceParm> m_parms;
	
	//service output
	private Map<String, HAPServiceResult> m_results;
	

	public HAPServiceInterface() {
		this.m_parms = new LinkedHashMap<String, HAPServiceParm>();
		this.m_results = new LinkedHashMap<String, HAPServiceResult>();
	}

	public Map<String, HAPServiceParm> getParms(){  return this.m_parms;   }
	public void addParm(String name, HAPServiceParm parm) { this.m_parms.put(name, parm);  }
	
	public Map<String, HAPServiceResult> getResults(){ return this.m_results;  }
	public void addResult(String name, HAPServiceResult result) {  this.m_results.put(name, result);  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			JSONArray resultArray = objJson.getJSONArray(RESULT);
			for(int i = 0; i<resultArray.length(); i++){
				HAPServiceResult resultEle = new HAPServiceResult();
				resultEle.buildObject(resultArray.get(i), HAPSerializationFormat.JSON);
				this.m_results.put(resultEle.getName(), resultEle);
			}
			
			JSONArray parmsArray = objJson.getJSONArray(PARM);
			for(int i = 0; i<parmsArray.length(); i++){
				HAPServiceParm parm = new HAPServiceParm();
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
