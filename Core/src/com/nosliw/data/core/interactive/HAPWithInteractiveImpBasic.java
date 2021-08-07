package com.nosliw.data.core.interactive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPWithInteractiveImpBasic extends HAPEntityInfoWritableImp implements HAPWithInteractive{

	//service input parms
	private List<HAPVariableInfo> m_requestParms;

	//service output
	private Map<String, HAPResultInteractive> m_results;
	
	public HAPWithInteractiveImpBasic() {
		this.m_requestParms = new ArrayList<HAPVariableInfo>();
		this.m_results = new LinkedHashMap<String, HAPResultInteractive>();
	}

	public void addRequestParm(HAPVariableInfo parm) { this.m_requestParms.add(parm);  }
	@Override
	public List<HAPVariableInfo> getRequestParms(){   return this.m_requestParms;   }
	
	@Override
	public Map<String, HAPResultInteractive> getResults(){ return this.m_results;  }
	public HAPResultInteractive getResult(String result) {   return this.m_results.get(result);  }
	public List<HAPOutputInteractive> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(String name, HAPResultInteractive result) {  this.m_results.put(name, result);  }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPVariableInfo parm : this.m_requestParms) 	parm.getDataInfo().process(runtimeEnv);
	}
	
	protected void cloneToWithInteractive(HAPWithInteractiveImpBasic withInteractive) {
		this.cloneToEntityInfo(withInteractive);
		for(HAPVariableInfo parm : this.m_requestParms) 	withInteractive.addRequestParm(parm.cloneVariableInfo());
		for(String resultName : this.m_results.keySet()) {
			withInteractive.addResult(resultName, this.m_results.get(resultName).cloneInteractiveResult());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			JSONObject resultObject = objJson.getJSONObject(RESULT);
			for(Object key : resultObject.keySet()) {
				String name = (String)key;
				HAPResultInteractive resultEle = new HAPResultInteractive();
				resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
				resultEle.setName(name);
				this.m_results.put(resultEle.getName(), resultEle);
			}
			
			JSONArray parmsArray = objJson.getJSONArray(REQUEST);
			for(int i=0; i<parmsArray.length(); i++) {
				HAPVariableInfo parm = HAPVariableInfo.buildVariableInfoFromObject(parmsArray.get(i));
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
		jsonMap.put(REQUEST, HAPJsonUtility.buildJson(this.m_requestParms, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}

}
