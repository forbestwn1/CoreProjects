package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPLog;

public class HAPLogStep extends HAPLog{

	private String m_name;
	
	private String m_stepId;
	
	private int m_stepIndex;
	
	private Map<String, HAPData> m_parms;
	
	private Map<String, HAPData> m_referencedData;
	
	private HAPResultStep m_result;
	
	public HAPLogStep(HAPExecutableStep step, Map<String, HAPData> parms, Map<String, HAPData> referencedData) {
		this.m_name = step.getName();
		this.m_stepIndex = step.getIndex();
		this.m_parms = new LinkedHashMap<String, HAPData>(parms);
		this.m_referencedData = new LinkedHashMap<String, HAPData>(referencedData);
	}
	
	public void setResult(HAPResultStep result) {	this.m_result = result;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put("type", "STEP");
		jsonMap.put("name", this.m_name);
		jsonMap.put("index", this.m_stepIndex+"");
		jsonMap.put("result", this.m_result.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put("parms", HAPJsonUtility.buildJson(m_parms, HAPSerializationFormat.JSON));
		jsonMap.put("referencedData", HAPJsonUtility.buildJson(this.m_referencedData, HAPSerializationFormat.JSON));
		jsonMap.put("children", HAPJsonUtility.buildJson(this.getChildren(), HAPSerializationFormat.JSON));
	}	
}
