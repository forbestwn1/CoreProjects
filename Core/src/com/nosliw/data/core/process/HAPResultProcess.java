package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;

@HAPEntityWithAttribute
public class HAPResultProcess extends HAPSerializableImp{

	@HAPAttribute
	public static final String RESULTNAME = "resultName";

	@HAPAttribute
	public static final String OUTPUT = "output";

	private String m_resultName;
	
	private Map<String, HAPData> m_output;
	
	public HAPResultProcess(String resultName, Map<String, HAPData> output) {
		this.m_resultName = resultName;
		this.m_output = output;
	}
	
	public String getResultName() {  return this.m_resultName;   }
	public Map<String, HAPData> getOutput(){   return this.m_output;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESULTNAME, this.m_resultName);
		jsonMap.put(OUTPUT, HAPJsonUtility.buildJson(m_output, HAPSerializationFormat.JSON));
	}
}
