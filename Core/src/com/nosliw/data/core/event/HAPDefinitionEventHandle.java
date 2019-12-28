package com.nosliw.data.core.event;

import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPIdProcess;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

//event process definition
public class HAPDefinitionEventHandle {

	//how to process event(inputmapping, process, outputmapping)
	private HAPDefinitionWrapperTask<HAPIdProcess> m_process; 
	
	//process suite resource
	private HAPDefinitionProcessSuite m_processSuite;
	
}
