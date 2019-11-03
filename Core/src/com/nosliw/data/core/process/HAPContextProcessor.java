package com.nosliw.data.core.process;

import java.util.Map;

//context for processing process
//it include related suite or process
public class HAPContextProcessor {

	private HAPDefinitionProcessSuite m_suite; 
	
	private HAPManagerProcessDefinition m_processDefMan;
	
	private Map<String, HAPDefinitionProcess> m_processes;
	
	
	public static HAPContextProcessor createContext(HAPDefinitionProcessSuite suite, HAPManagerProcessDefinition processDefMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processDefMan = processDefMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextProcessor createContext(Map<String, HAPDefinitionProcess> processes, HAPManagerProcessDefinition processDefMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processDefMan = processDefMan;
		out.m_processes = processes;
		return out;
	}
	
	public static HAPContextProcessor createContext(HAPManagerProcessDefinition processDefMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processDefMan = processDefMan;
		return out;
	}
	
	public HAPDefinitionProcessWithContext getProcessDefinition(String processId) {
		HAPDefinitionProcessWithContext out = null;
		HAPIdProcess id = new HAPIdProcess(processId);
		if(id.getSuiteId()!=null) {
			HAPDefinitionProcessSuite suite = this.m_processDefMan.getProcessSuite(id.getSuiteId());
			out = new HAPDefinitionProcessWithContext(suite.getProcess(id.getProcessId()), HAPContextProcessor.createContext(suite, m_processDefMan));
		}
		else {
			if(this.m_suite!=null) {
				out = new HAPDefinitionProcessWithContext(this.m_suite.getProcess(id.getProcessId()), HAPContextProcessor.createContext(this.m_suite, m_processDefMan));
			}
			else if(this.m_processes!=null){
				out = new HAPDefinitionProcessWithContext(this.m_processes.get(id.getProcessId()), HAPContextProcessor.createContext(this.m_processes, m_processDefMan));
			}
		}
		return out;
	}
	
}

