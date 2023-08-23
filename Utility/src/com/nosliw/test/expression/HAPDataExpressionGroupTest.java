package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskDataExpression;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteDataExpressionRhino;

public class HAPDataExpressionGroupTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPInfoRuntimeTaskDataExpression taskInfo = new HAPInfoRuntimeTaskDataExpression("test1", "reference");
		HAPRuntimeTaskExecuteDataExpressionRhino task = new HAPRuntimeTaskExecuteDataExpressionRhino(taskInfo, runtimeEnvironment);

		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
