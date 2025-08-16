package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.xxx.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskGroupItemResource;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoTaskGroupItemResource;

public class HAPScriptExpressionGroupTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPInfoRuntimeTaskTaskGroupItemResource taskInfo = new HAPInfoRuntimeTaskTaskGroupItemResource(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, "test1", "literate_datascript", Object.class);
		HAPRuntimeTaskExecuteRhinoTaskGroupItemResource task = new HAPRuntimeTaskExecuteRhinoTaskGroupItemResource(taskInfo, runtimeEnvironment);

		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
