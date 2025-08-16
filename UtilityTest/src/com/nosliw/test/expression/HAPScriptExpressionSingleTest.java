package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.xxx.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskResource;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoTaskResource;

public class HAPScriptExpressionSingleTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPInfoRuntimeTaskTaskResource taskInfo = new HAPInfoRuntimeTaskTaskResource(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, "test1", Object.class);
		HAPRuntimeTaskExecuteRhinoTaskResource task = new HAPRuntimeTaskExecuteRhinoTaskResource(taskInfo, runtimeEnvironment);
		
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
