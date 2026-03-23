package com.nosliw.core.data;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtime.HAPRuntimeManager;
import com.nosliw.core.runtime.execute.HAPExecutorRuntime;
import com.nosliw.core.runtime.execute.HAPTaskRuntime;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeTaskExecuteDataOperationRhino;

public class HAPUtilityExecuteTask {

	public static HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation,
			List<HAPOperationParm> parmsData, HAPExecutorRuntime runtime) {

		HAPRuntimeInfo runtimeInfo = runtime.getRuntimeInfo();
		HAPTaskRuntime task = null;

		if (runtimeInfo.equals(HAPRuntimeManager.RUNTIME_JS_RHION)) {
			task = new HAPRuntimeTaskExecuteDataOperationRhino(dataTypeId, operation, parmsData);
		}

		HAPServiceData serviceData = runtime.executeTaskSync(task);
		return serviceData;
	}

//	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData) {
//		HAPExecutableEntityExpressionDataGroup expression = this.getRuntimeEnvironment().getExpressionManager()
//				.getExpression(expressionStr);
//		HAPTaskRuntime task = new HAPRuntimeTaskExecuteRhinoDataExpressionGroup(
//				new HAPInfoRuntimeTaskDataExpressionGroup(expression, null, parmsData, null),
//				this.getRuntimeEnvironment());
//		HAPServiceData serviceData = this.executeTaskSync(task);
//		return serviceData;
//	}

}
