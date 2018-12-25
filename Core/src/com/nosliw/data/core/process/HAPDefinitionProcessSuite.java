package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextGroup;

//application that contains multiple tasks
@HAPEntityWithAttribute
public class HAPDefinitionProcessSuite extends HAPEntityInfoImp{

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String PROCESS = "process";

	private String m_id;
	
	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_context;

	private Map<String, HAPDefinitionProcess> m_processes;
	
	public HAPDefinitionProcessSuite() {
		this.m_processes = new LinkedHashMap<String, HAPDefinitionProcess>();
	}

	public String getId() {  return this.m_id;   }
	
	public HAPContextGroup getContext() {   return this.m_context;   }
	
	public Map<String, HAPDefinitionProcess> getProcesses(){   return this.m_processes;   }
	
	public void addProcess(String id, HAPDefinitionProcess process) {  this.m_processes.put(id, process);  }

	public void setContext(HAPContextGroup context) {  this.m_context = context;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}
}
