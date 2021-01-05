package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;

public interface HAPProcessorActivity {

	/**
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPExecutableProcess processExe,
			HAPContextGroup context,
			Map<String, HAPExecutableDataAssociation> results,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processManager,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureContextProcessor configure, 
			HAPProcessTracker processTracker
	);

}
