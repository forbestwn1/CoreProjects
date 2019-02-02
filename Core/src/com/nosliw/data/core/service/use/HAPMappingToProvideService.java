package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

public class HAPMappingToProvideService extends HAPSerializableImp{

	private String m_provideServiceName;
	
	private HAPServiceInterface m_serviceInterface;
	
	private HAPContext m_parmMapping;
	
	private Map<String, HAPContext> m_resultMapping;
	
	public HAPMappingToProvideService() {
		this.m_parmMapping = new HAPContext();
		this.m_resultMapping = new LinkedHashMap<String, HAPContext>();
	}
	
	
	
	
}
