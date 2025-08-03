package com.nosliw.core.application.common.interactive;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPResultInteractiveTask extends HAPSerializableImp{

	@HAPAttribute
	public static final String RESULTNAME = "resultName";

	@HAPAttribute
	public static final String RESULTVALUE = "resultValue";

	private String m_resultName;
	
	private Map<String, HAPData> m_resultValue;

	public HAPResultInteractiveTask() {
		this.m_resultValue = new LinkedHashMap<String, HAPData>();
	}
	
	public HAPResultInteractiveTask(String resultName, Map<String, HAPData> output) {
		this.m_resultName = resultName;
		this.m_resultValue = output;
	}
	
	public String getResultName() {  return this.m_resultName;   }
	public Map<String, HAPData> getResultValue(){   return this.m_resultValue;   }

	@Override
	protected boolean buildObjectByJson(Object json){ 
		JSONObject jsonObj = (JSONObject)json;
		this.m_resultName = jsonObj.optString(RESULTNAME);
		JSONObject outputObj = jsonObj.optJSONObject(RESULTVALUE);
		if(outputObj!=null) {
			for(Object key : outputObj.keySet()) {
				String name = (String)key;
				this.m_resultValue.put(name, HAPUtilityData.buildDataWrapperFromJson(outputObj.getJSONObject(name)));
			}
		}
		return false;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESULTNAME, this.m_resultName);
		jsonMap.put(RESULTVALUE, HAPUtilityJson.buildJson(m_resultValue, HAPSerializationFormat.JSON));
	}
}
