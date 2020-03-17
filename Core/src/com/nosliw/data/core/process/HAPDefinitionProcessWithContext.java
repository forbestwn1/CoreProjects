package com.nosliw.data.core.process;

import com.nosliw.data.core.resource.HAPResourceDefinitionWithContext;

public class HAPDefinitionProcessWithContext extends HAPResourceDefinitionWithContext{

	//process itself
//	private HAPDefinitionProcess m_process;
	
	//context that the process depend on
//	private HAPContextProcessor m_context;
	
	public HAPDefinitionProcessWithContext(HAPDefinitionProcess processDef, HAPContextProcessor context) {
		super(processDef, context);
	}
	
	public HAPDefinitionProcessWithContext(HAPDefinitionProcess processDef) {
		this(processDef, null);
	}
	
//	public HAPDefinitionProcess getProcess() {  return (HAPDefinitionProcess)this.getResourceDefinition();	}
	
//	public HAPContextProcessor getContext() {  return (HAPContextProcessor)this.getResourceContext();   }
	
}
