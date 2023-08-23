package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskDataExpressionSingle;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoDataExpressionSingle;

public class HAPDataExpressionSingleTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPInfoRuntimeTaskDataExpressionSingle taskInfo = new HAPInfoRuntimeTaskDataExpressionSingle("test1");
		HAPRuntimeTaskExecuteRhinoDataExpressionSingle task = new HAPRuntimeTaskExecuteRhinoDataExpressionSingle(taskInfo, runtimeEnvironment);

		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
