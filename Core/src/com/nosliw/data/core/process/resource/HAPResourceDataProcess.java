package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

public class HAPResourceDataProcess extends HAPSerializableImp implements HAPResourceDataJSValue{

	private HAPExecutableProcess m_process;
	
	public HAPResourceDataProcess(HAPExecutableProcess process){
		this.m_process = process;
	}
	
	public HAPExecutableProcess getProcess(){ return this.m_process;  }
	
	@Override
	public String getValue() {
		return this.m_process.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
