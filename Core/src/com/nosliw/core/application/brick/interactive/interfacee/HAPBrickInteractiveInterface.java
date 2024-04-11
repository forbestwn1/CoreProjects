package com.nosliw.core.application.brick.interactive.interfacee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPBrickInteractiveInterface extends HAPBrick{

	@HAPAttribute
	public static String REQUEST = "request";
	
	@HAPAttribute
	public static String RESULT = "result";

	//service input parms
	private List<HAPRequestParmInInteractiveInterface> m_requestParms;

	//service output
	private Map<String, HAPResultInInteractiveInterface> m_results;
	
	public HAPBrickInteractiveInterface() {
		this.m_requestParms = new ArrayList<HAPRequestParmInInteractiveInterface>();
		this.m_results = new LinkedHashMap<String, HAPResultInInteractiveInterface>();
	}

	public void addRequestParm(HAPRequestParmInInteractiveInterface parm) { this.m_requestParms.add(parm);  }
	public List<HAPRequestParmInInteractiveInterface> getRequestParms(){   return this.m_requestParms;   }
	
	public Map<String, HAPResultInInteractiveInterface> getResults(){ return this.m_results;  }
	public HAPResultInInteractiveInterface getResult(String result) {   return this.m_results.get(result);  }
	public List<HAPResultOutputInInteractiveInterface> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(String name, HAPResultInInteractiveInterface result) {  this.m_results.put(name, result);  }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPRequestParmInInteractiveInterface parm : this.m_requestParms) {
			parm.getDataInfo().process(runtimeEnv);
		}
	}
	
//	protected void cloneToInteractive(HAPBrickInteractiveInterface interactive) {
//		this.cloneToEntityInfo(interactive);
//		for(HAPRequestParmInInteractiveInterface parm : this.m_requestParms) {
//			interactive.addRequestParm(parm.cloneVariableInfo());
//		}
//		for(String resultName : this.m_results.keySet()) {
//			interactive.addResult(resultName, this.m_results.get(resultName).cloneInteractiveResult());
//		}
//	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			JSONObject resultObject = objJson.getJSONObject(RESULT);
			for(Object key : resultObject.keySet()) {
				String name = (String)key;
				HAPResultInInteractiveInterface resultEle = new HAPResultInInteractiveInterface();
				resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
				resultEle.setName(name);
				this.m_results.put(resultEle.getName(), resultEle);
			}
			
			JSONArray parmsArray = objJson.getJSONArray(REQUEST);
			for(int i=0; i<parmsArray.length(); i++) {
				HAPRequestParmInInteractiveInterface parm = HAPRequestParmInInteractiveInterface.buildParmFromObject(parmsArray.get(i));
				this.addRequestParm(parm);
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
		jsonMap.put(REQUEST, HAPUtilityJson.buildJson(this.m_requestParms, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPUtilityJson.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}

}
