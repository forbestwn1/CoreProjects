package com.nosliw.core.application.brick.interactive.interfacee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveTask;

@HAPEntityWithAttribute
public class HAPBlockInteractiveInterface extends HAPBrickBlockSimple implements HAPInteractiveTask{

	@Override
	public void init() {
		this.setAttributeValueWithValue(REQUEST, new ArrayList<HAPRequestParmInInteractive>());
		this.setAttributeValueWithValue(REQUEST, new LinkedHashMap<String, HAPResultInInteractiveTask>());
	}
	
	public void addRequestParm(HAPRequestParmInInteractive parm) { this.getRequestParms().add(parm);  }
	@Override
	public List<HAPRequestParmInInteractive> getRequestParms(){   return (List<HAPRequestParmInInteractive>)this.getAttributeValueOfValue(REQUEST);   }
	
	@Override
	public Map<String, HAPResultInInteractiveTask> getResults(){ return (Map<String, HAPResultInInteractiveTask>)this.getAttributeValueOfValue(RESULT);  }
	public HAPResultInInteractiveTask getResult(String result) {   return this.getResults().get(result);  }
	public List<HAPResultElementInInteractiveTask> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(HAPResultInInteractiveTask result) {  this.getResults().put(result.getName(), result);  }
	
//	public void process(HAPRuntimeEnvironment runtimeEnv) {
//		for(HAPRequestParmInInteractive parm : this.getRequestParms()) {
//			parm.getDataInfo().process(runtimeEnv);
//		}
//	}
	
//	protected void cloneToInteractive(HAPBlockInteractiveInterface interactive) {
//		this.cloneToEntityInfo(interactive);
//		for(HAPRequestParmInInteractive parm : this.m_requestParms) {
//			interactive.addRequestParm(parm.cloneVariableInfo());
//		}
//		for(String resultName : this.m_results.keySet()) {
//			interactive.addResult(resultName, this.m_results.get(resultName).cloneInteractiveResult());
//		}
//	}
	
    @Override
	protected Object buildAttributeValueFormatJson(String attrName, Object obj) {
    	Object out = null;
    	if(REQUEST.equals(attrName)) {
    		List<HAPRequestParmInInteractive> rquestParms = new ArrayList<HAPRequestParmInInteractive>();
			JSONArray parmsArray = (JSONArray)obj;
			for(int i=0; i<parmsArray.length(); i++) {
				HAPRequestParmInInteractive parm = HAPRequestParmInInteractive.buildParmFromObject(parmsArray.get(i));
				rquestParms.add(parm);
				out = rquestParms;
			}
    	}
    	else if(RESULT.equals(attrName)) {
    		Map<String, HAPResultInInteractiveTask> results = new LinkedHashMap<String, HAPResultInInteractiveTask>();
			JSONObject resultObject = (JSONObject)obj;
			for(Object key : resultObject.keySet()) {
				String name = (String)key;
				HAPResultInInteractiveTask resultEle = new HAPResultInInteractiveTask();
				resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
				resultEle.setName(name);
				results.put(resultEle.getName(), resultEle);
			}
			out = results;
    	}
    	return out;
    }
}
