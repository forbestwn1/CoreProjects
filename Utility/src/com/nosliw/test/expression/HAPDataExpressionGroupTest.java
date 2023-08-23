package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskDataExpressionGroup;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoDataExpressionGroup;

public class HAPDataExpressionGroupTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPInfoRuntimeTaskDataExpressionGroup taskInfo = new HAPInfoRuntimeTaskDataExpressionGroup("test1", "reference");
		HAPRuntimeTaskExecuteRhinoDataExpressionGroup task = new HAPRuntimeTaskExecuteRhinoDataExpressionGroup(taskInfo, runtimeEnvironment);

		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
