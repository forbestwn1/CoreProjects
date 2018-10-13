package com.nosliw.data.core.task111;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;

public class HAPLogTask extends HAPLog{

	private String m_name;
	
	private String m_id;
	
	private HAPData m_result;

	private Map<String, HAPData> m_parms;
	
	public HAPLogTask() {
		this.m_parms = new LinkedHashMap<String, HAPData>();
	}

	public void setTaskExecutable(HAPExecutableTask exeTask) {		this.m_name = exeTask.getName();	}
	
	public void setParms(Map<String, HAPData> parms) {
		this.m_parms.clear();
		this.m_parms.putAll(parms);  
	}
	
	public void setResult(HAPData result) {  this.m_result = result;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put("type", "TASK");
		jsonMap.put("name", this.m_name);
		if(this.m_result!=null) {
			jsonMap.put("result", this.m_result.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put("parms", HAPJsonUtility.buildJson(m_parms, HAPSerializationFormat.JSON));
		jsonMap.put("children", HAPJsonUtility.buildJson(this.getChildren(), HAPSerializationFormat.JSON));
	}	
}
