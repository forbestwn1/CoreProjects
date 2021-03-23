package com.nosliw.data.core.process;

import com.nosliw.data.core.resource.HAPContextResourceDefinition;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResource;

//context for processing process
//it include related suite or process
public class HAPContextProcessor implements HAPContextResourceDefinition{

	private HAPDefinitionProcessSuite m_suite; 
	
	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public static HAPContextProcessor createContext(HAPDefinitionProcessSuite suite, HAPManagerResourceDefinition resourceDefMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_resourceDefMan = resourceDefMan;
		out.m_suite = suite;
		return out;
	}
	
	public static HAPContextProcessor createContext(HAPManagerResourceDefinition resourceDefMan) {
		HAPContextProcessor out = new HAPContextProcessor();
		out.m_resourceDefMan = resourceDefMan;
		return out;
	}
	
	@Override
	public HAPEntityWithResourceContext getResourceDefinition(HAPResourceId processId) {
		HAPDefinitionProcess processDef = (HAPDefinitionProcess)HAPUtilityResource.getImpliedResourceDefinition(processId, this.m_suite, this.m_resourceDefMan);
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(processDef, HAPContextProcessor.createContext(processDef.getSuite(), m_resourceDefMan));
		return out;
	}	
}
