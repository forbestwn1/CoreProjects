package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextGroup;

//application that contains multiple tasks
public class HAPDefinitionProcessSuite extends HAPEntityInfoImp{

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String PROCESS = "process";

	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_context;

	private List<HAPDefinitionProcess> m_processes;
	
	public HAPDefinitionProcessSuite() {
		this.m_processes = new ArrayList<HAPDefinitionProcess>();
	}

	public void addProcess(HAPDefinitionProcess process) {  this.m_processes.add(process);  }

	public void setContext(HAPContextGroup context) {  this.m_context = context;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}
}
