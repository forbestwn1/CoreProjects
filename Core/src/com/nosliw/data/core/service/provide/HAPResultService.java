package com.nosliw.data.core.service.provide;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

@HAPEntityWithAttribute
public class HAPResultService extends HAPSerializableImp{

	@HAPAttribute
	public static final String RESULTNAME = "resultName";

	@HAPAttribute
	public static final String OUTPUT = "output";

	private String m_resultName;
	
	private Map<String, HAPData> m_output;

	public HAPResultService() {
		this.m_output = new LinkedHashMap<String, HAPData>();
	}
	
	public HAPResultService(String resultName, Map<String, HAPData> output) {
		this.m_resultName = resultName;
		this.m_output = output;
	}
	
	public String getResultName() {  return this.m_resultName;   }
	public Map<String, HAPData> getOutput(){   return this.m_output;   }

	@Override
	protected boolean buildObjectByJson(Object json){ 
		JSONObject jsonObj = (JSONObject)json;
		this.m_resultName = jsonObj.optString(RESULTNAME);
		JSONObject outputObj = jsonObj.optJSONObject(OUTPUT);
		if(outputObj!=null) {
			for(Object key : outputObj.keySet()) {
				String name = (String)key;
				this.m_output.put(name, HAPDataUtility.buildDataWrapperFromJson(outputObj.getJSONObject(name)));
			}
		}
		return false;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESULTNAME, this.m_resultName);
		jsonMap.put(OUTPUT, HAPJsonUtility.buildJson(m_output, HAPSerializationFormat.JSON));
	}
}
