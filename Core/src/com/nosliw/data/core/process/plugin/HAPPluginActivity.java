package com.nosliw.data.core.process.plugin;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.process.HAPContextProcessor;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

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
			HAPExecutableProcess processExe,
			HAPValueStructureDefinitionGroup context,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker
	);
	
	HAPDefinitionActivity buildActivityDefinition(Object obj);
	
	HAPExecutableActivity buildActivityExecutable(Object obj);
	
	HAPJsonTypeScript getScript(String env);
}
