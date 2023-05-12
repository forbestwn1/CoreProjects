package com.nosliw.data.core.domain.common.interactive;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPDefinitionInteractiveImpBasic extends HAPEntityInfoWritableImp implements HAPDefinitionInteractive{

	//service input parms
	private List<HAPDefinitionInteractiveRequestParm> m_requestParms;

	//service output
	private Map<String, HAPDefinitionInteractiveResult> m_results;
	
	public HAPDefinitionInteractiveImpBasic() {
		this.m_requestParms = new ArrayList<HAPDefinitionInteractiveRequestParm>();
		this.m_results = new LinkedHashMap<String, HAPDefinitionInteractiveResult>();
	}

	public void addRequestParm(HAPDefinitionInteractiveRequestParm parm) { this.m_requestParms.add(parm);  }
	@Override
	public List<HAPDefinitionInteractiveRequestParm> getRequestParms(){   return this.m_requestParms;   }
	
	@Override
	public Map<String, HAPDefinitionInteractiveResult> getResults(){ return this.m_results;  }
	public HAPDefinitionInteractiveResult getResult(String result) {   return this.m_results.get(result);  }
	public List<HAPDefinitionInteractiveResultOutput> getResultOutput(String result) {  return this.getResult(result).getOutput();  }
	public void addResult(String name, HAPDefinitionInteractiveResult result) {  this.m_results.put(name, result);  }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionInteractiveRequestParm parm : this.m_requestParms) 	parm.getDataInfo().process(runtimeEnv);
	}
	
	protected void cloneToWithInteractive(HAPDefinitionInteractiveImpBasic withInteractive) {
		this.cloneToEntityInfo(withInteractive);
		for(HAPDefinitionInteractiveRequestParm parm : this.m_requestParms) 	withInteractive.addRequestParm(parm.cloneVariableInfo());
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
				HAPDefinitionInteractiveResult resultEle = new HAPDefinitionInteractiveResult();
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
		jsonMap.put(REQUEST, HAPUtilityJson.buildJson(this.m_requestParms, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPUtilityJson.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}

}
