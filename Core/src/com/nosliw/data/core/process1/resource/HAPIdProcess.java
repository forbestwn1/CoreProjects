package com.nosliw.data.core.process1.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdProcess extends HAPSerializableImp{

	private String m_processId;
	private String m_suiteId;
	
	public HAPIdProcess(String suiteId, String processId){
		this.m_processId = processId;
		this.m_suiteId = suiteId;
	}

	public HAPIdProcess(String id){
		this.parseId(id);
	}

	public String getId(){  return HAPUtilityNamingConversion.cascadeLevel1(m_processId, m_suiteId);  }

	public String getSuiteId() {   return this.m_suiteId;   }
	public String getProcessId() {    return this.m_processId;    }
	
	private void parseId(String id) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(id);
		this.m_processId = segs[0];
		if(segs.length>1)	this.m_suiteId = segs[1];
	}
	
	@Override
	protected String buildLiterate(){		return this.getId();	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.parseId(literateValue);
		return true;
	}
}
