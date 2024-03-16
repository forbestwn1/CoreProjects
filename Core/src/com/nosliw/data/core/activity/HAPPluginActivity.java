package com.nosliw.data.core.activity;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.division.manual.HAPManualEntityComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityWrapperValueStructure;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//each type of activity should provide a plugin which contains information:
//   how to parse activity definition
//	 how to process definition to executable
//   script for the runtime
@HAPEntityWithAttribute
public interface HAPPluginActivity {

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String SCRIPT = "script";
	
	String getType();
	
	//process activity definition to executable
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPDefinitionEntityWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure configure, 
			HAPProcessTracker processTracker);
	
	HAPDefinitionActivity buildActivityDefinition(Object obj, HAPManualEntityComplex complexEntity);
	
	HAPExecutableActivity buildActivityExecutable(Object obj);
	
	HAPJsonTypeScript getScript(String env);
}
