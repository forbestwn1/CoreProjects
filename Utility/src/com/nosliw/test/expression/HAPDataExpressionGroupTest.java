package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskDataExpression;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteDataExpressionRhino;

public class HAPDataExpressionGroupTest {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, "test1");
		HAPExecutablePackage executablePackage = runtimeEnvironment.getDomainEntityExecutableManager().getExecutablePackage(resourceId);

		HAPInfoRuntimeTaskDataExpression taskInfo = new HAPInfoRuntimeTaskDataExpression(executablePackage, "reference");
		HAPRuntimeTaskExecuteDataExpressionRhino task = new HAPRuntimeTaskExecuteDataExpressionRhino(taskInfo, runtimeEnvironment);

		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println("--------------------   -------------------------");
		System.out.println(out);
		System.out.println();
	}
}
