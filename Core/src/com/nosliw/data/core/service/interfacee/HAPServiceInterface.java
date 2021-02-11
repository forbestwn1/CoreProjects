package com.nosliw.data.core.service.interfacee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPServiceInterface extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	@HAPAttribute
	public static String RESULT = "result";
	
	//service input parms
	private List<HAPServiceParm> m_parms;

	//service output
	private Map<String, HAPServiceResult> m_results;
	
	public HAPServiceInterface() {
		this.m_parms = new ArrayList<HAPServiceParm>();
		this.m_results = new LinkedHashMap<String, HAPServiceResult>();
	}

	public void addParm(HAPServiceParm parm) { this.m_parms.add(parm);  }
	public List<HAPServiceParm> getParms(){   return this.m_parms;   }
	
	public Map<String, HAPServiceResult> getResults(){ return this.m_results;  }
	public HAPServiceResult getResult(String result) {   return this.m_results.get(result);  }
	public List<HAPServiceOutput> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(String name, HAPServiceResult result) {  this.m_results.put(name, result);  }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPServiceParm parm : this.m_parms) 	parm.getDataInfo().process(runtimeEnv);
		for(HAPServiceResult result : this.m_results.values()) {
			for(HAPServiceOutput output : result.getOutput()) {
				output.getDataInfo().process(runtimeEnv);
			}
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			JSONObject resultObject = objJson.getJSONObject(RESULT);
			for(Object key : resultObject.keySet()) {
				String name = (String)key;
				HAPServiceResult resultEle = new HAPServiceResult();
				resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
				resultEle.setName(name);
				this.m_results.put(resultEle.getName(), resultEle);
			}
			
			JSONArray parmsArray = objJson.getJSONArray(PARM);
			for(int i=0; i<parmsArray.length(); i++) {
				HAPServiceParm parm = new HAPServiceParm();
				parm.buildObject(parmsArray.get(i), HAPSerializationFormat.JSON);
				this.addParm(parm);
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
		jsonMap.put(PARM, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}

}
