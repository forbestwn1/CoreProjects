package com.nosliw.data.core.process.plugin;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroupExecutable;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;

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
			HAPExecutableProcess processExe,
			HAPContextGroup context,
			Map<String, HAPDefinitionDataAssociationGroupExecutable> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPEnvContextProcessor envContextProcessor,
			HAPProcessContext processContext
	);
	
	HAPDefinitionActivity buildActivityDefinition(Object obj);
	
	HAPScript getScript(String env);
}
