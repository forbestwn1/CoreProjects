package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionParm;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPInteractiveTask extends HAPSerializableImp implements HAPInteractive{

	private HAPInteractiveRequest m_request;

	private List<HAPInteractiveResultTask> m_results;

	public HAPInteractiveTask() {
		this.m_results = new ArrayList<HAPInteractiveResultTask>();
		this.m_request = new HAPInteractiveRequest();
	}
	
	public void setRequest(HAPInteractiveRequest request) {    this.m_request = request;      }
	public List<HAPDefinitionParm> getRequestParms() {   return this.m_request.getRequestParms();  }

	public List<HAPInteractiveResultTask> getResult() {   return this.m_results;  }
	public void addResult(HAPInteractiveResultTask result) {    this.m_results.add(result);    }
	
	public HAPInteractiveResultTask getResult(String resultName) {
		for(HAPInteractiveResultTask result : this.m_results) {
			if(result.getName().equals(resultName)) {
				return result;
			}
		}
		return null;
	}
	
	public static HAPInteractiveTask parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInteractiveTask out = new HAPInteractiveTask();
		
		JSONArray parmsArray = jsonObj.optJSONArray(REQUEST);
		if(parmsArray!=null) {
			out.setRequest(HAPInteractiveRequest.parse(parmsArray, dataRuleMan));
		}
		
		Object resutltsObj = jsonObj.opt(RESULT);
		if(resutltsObj!=null) {
			if(resutltsObj instanceof JSONObject) {
				JSONObject resultObject = (JSONObject)resutltsObj;
				for(Object key : resultObject.keySet()) {
					String name = (String)key;
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
					resultEle.setName(name);
					out.addResult(resultEle);
				}
			}
			else if(resutltsObj instanceof JSONArray) {
				JSONArray resultArray = (JSONArray)resutltsObj;
				for(int i=0; i<resultArray.length(); i++) {
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultArray.getJSONObject(i), HAPSerializationFormat.JSON);
					out.addResult(resultEle);
				}
			}
		}
		
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray parmsArray = jsonObj.optJSONArray(REQUEST);
		if(parmsArray!=null) {
			this.m_request.buildObject(parmsArray, HAPSerializationFormat.JSON);
		}
		
		Object resutltsObj = jsonObj.opt(RESULT);
		if(resutltsObj!=null) {
			if(resutltsObj instanceof JSONObject) {
				JSONObject resultObject = (JSONObject)resutltsObj;
				for(Object key : resultObject.keySet()) {
					String name = (String)key;
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
					resultEle.setName(name);
					this.m_results.add(resultEle);
				}
			}
			else if(resutltsObj instanceof JSONArray) {
				JSONArray resultArray = (JSONArray)resutltsObj;
				for(int i=0; i<resultArray.length(); i++) {
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultArray.getJSONObject(i), HAPSerializationFormat.JSON);
					this.m_results.add(resultEle);
				}
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(REQUEST, HAPManagerSerialize.getInstance().toStringValue(this.m_request, HAPSerializationFormat.JSON));
		
		Map<String, String> resultMapStr = new LinkedHashMap<String, String>();
		for(HAPInteractiveResultTask result : this.m_results) {
			resultMapStr.put(result.getName(), result.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(RESULT, HAPUtilityJson.buildMapJson(resultMapStr));
	}
	
	
//	public void process(HAPRuntimeEnvironment runtimeEnv) {
//	for(HAPRequestParmInInteractive parm : this.getRequestParms()) {
//		parm.getDataInfo().process(runtimeEnv);
//	}
//}

//protected void cloneToInteractive(HAPBlockInteractiveInterfaceExpression interactive) {
//	this.cloneToEntityInfo(interactive);
//	for(HAPRequestParmInInteractive parm : this.m_requestParms) {
//		interactive.addRequestParm(parm.cloneVariableInfo());
//	}
//	for(String resultName : this.m_results.keySet()) {
//		interactive.addResult(resultName, this.m_results.get(resultName).cloneInteractiveResult());
//	}
//}


}
