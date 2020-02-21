package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.resource.HAPResourceIdProcess;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//context for processing process
//it include related suite or process
public class HAPContextProcessor {

	private HAPDefinitionProcessSuite m_suite; 
	
	private HAPManagerProcess m_processMan;
	
	private Map<String, HAPDefinitionProcessSuiteElementEntity> m_processes;
	
	public static HAPContextProcessor createContext(HAPDefinitionProcessSuite suite, HAPManagerProcess processMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processMan = processMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextProcessor createContext(Map<String, HAPDefinitionProcessSuiteElementEntity> processes, HAPManagerProcess processMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processMan = processMan;
		out.m_processes = processes;
		return out;
	}
	
	public static HAPContextProcessor createContext(HAPManagerProcess processMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_processMan = processMan;
		return out;
	}
	
	public HAPDefinitionProcessWithContext getProcessDefinition(HAPResourceId processId) {
		HAPDefinitionProcessWithContext out = null;
		HAPDefinitionProcess processDef = null;
		if(processId.getStructure().equals(HAPConstant.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdProcess processResourceId = new HAPResourceIdProcess((HAPResourceIdSimple)processId);
			if(!HAPBasicUtility.isStringEmpty(processResourceId.getProcessId().getSuiteId())) {
				processDef = this.m_processMan.getProcessDefinition(processId, null);
			}
			else {
				processDef = new HAPDefinitionProcess(this.m_suite, processResourceId.getProcessId().getProcessId());
			}
		}
		else {
			processDef = this.m_processMan.getProcessDefinition(processId, null);
		}
		out = new HAPDefinitionProcessWithContext(processDef, HAPContextProcessor.createContext(processDef.getSuite(), m_processMan));
		return out;
	}	
	

//	public HAPDefinitionProcessWithContext getProcessDefinition(String processId) {
//		HAPDefinitionProcessWithContext out = null;
//		HAPProcessId id = new HAPProcessId(processId);
//		if(id.getSuiteId()!=null) {
//			HAPDefinitionProcessSuite suite = this.m_processDefMan.getProcessSuite(id.getSuiteId());
//			out = new HAPDefinitionProcessWithContext(suite.getProcess(id.getProcessId()), HAPContextProcessor.createContext(suite, m_processDefMan));
//		}
//		else {
//			if(this.m_suite!=null) {
//				out = new HAPDefinitionProcessWithContext(this.m_suite.getProcess(id.getProcessId()), HAPContextProcessor.createContext(this.m_suite, m_processDefMan));
//			}
//			else if(this.m_processes!=null){
//				out = new HAPDefinitionProcessWithContext(this.m_processes.get(id.getProcessId()), HAPContextProcessor.createContext(this.m_processes, m_processDefMan));
//			}
//		}
//		return out;
//	}	
}

