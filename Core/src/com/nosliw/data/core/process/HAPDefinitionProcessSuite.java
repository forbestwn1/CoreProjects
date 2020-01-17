package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

//application that contains multiple tasks
@HAPEntityWithAttribute
public class HAPDefinitionProcessSuite extends HAPComponentImp{

	@HAPAttribute
	public static String PROCESS = "process";

	private Map<String, HAPDefinitionProcess> m_processes;
	
	public HAPDefinitionProcessSuite() {
		this.m_processes = new LinkedHashMap<String, HAPDefinitionProcess>();
	}

	public HAPDefinitionProcess getProcess(String processId) {  return this.m_processes.get(processId);   }
	public Map<String, HAPDefinitionProcess> getProcesses(){   return this.m_processes;   }
	
	public void addProcess(String id, HAPDefinitionProcess process) {
		process.getAttachmentContainer().merge(this.getAttachmentContainer(), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		this.m_processes.put(id, process);  
	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}

}
