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
import com.nosliw.core.application.HAPBrickImp;
import com.nosliw.core.application.common.interactive.HAPInteractive;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractiveInterface;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveInterface;
import com.nosliw.core.application.common.interactive.HAPResultOutputInInteractiveInterface;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPBrickInteractiveInterface extends HAPBrickImp implements HAPInteractive{

	@HAPAttribute
	public static String REQUEST = "request";
	
	@HAPAttribute
	public static String RESULT = "result";

	@Override
	public void init() {
		this.setAttributeValueWithValue(REQUEST, new ArrayList<HAPRequestParmInInteractiveInterface>());
		this.setAttributeValueWithValue(REQUEST, new LinkedHashMap<String, HAPResultInInteractiveInterface>());
	}
	
	public void addRequestParm(HAPRequestParmInInteractiveInterface parm) { this.getRequestParms().add(parm);  }
	public List<HAPRequestParmInInteractiveInterface> getRequestParms(){   return (List<HAPRequestParmInInteractiveInterface>)this.getAttributeValueOfValue(REQUEST);   }
	
	public Map<String, HAPResultInInteractiveInterface> getResults(){ return (Map<String, HAPResultInInteractiveInterface>)this.getAttributeValueOfValue(RESULT);  }
	public HAPResultInInteractiveInterface getResult(String result) {   return this.getResults().get(result);  }
	public List<HAPResultOutputInInteractiveInterface> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(HAPResultInInteractiveInterface result) {  this.getResults().put(result.getName(), result);  }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPRequestParmInInteractiveInterface parm : this.getRequestParms()) {
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
	protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {
    	if(REQUEST.equals(attrName)) {
			JSONArray parmsArray = (JSONArray)obj;
			for(int i=0; i<parmsArray.length(); i++) {
				HAPRequestParmInInteractiveInterface parm = HAPRequestParmInInteractiveInterface.buildParmFromObject(parmsArray.get(i));
				this.addRequestParm(parm);
			}
    	}
    	else if(RESULT.equals(attrName)) {
			JSONObject resultObject = (JSONObject)obj;
			for(Object key : resultObject.keySet()) {
				String name = (String)key;
				HAPResultInInteractiveInterface resultEle = new HAPResultInInteractiveInterface();
				resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
				resultEle.setName(name);
				this.addResult(resultEle);
			}
    	}
    	return true;
    }
}
