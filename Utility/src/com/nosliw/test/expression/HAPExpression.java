package com.nosliw.test.expression;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.script.context.data.HAPContextDataFlat;

public class HAPExpression {

	public static void main(String[] args) {

		try {
			String suite = "test1";
			String id = "test6";
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			HAPExecutableExpression expressionExe = runtimeEnvironment.getExpressionManager().getExpression(HAPUtilityExpressionResource.buildResourceId(suite, id), null, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));

			HAPContextDataFlat input = HAPUtilityComponent.getTestDataFromAttachment(expressionExe.getDefinition(), testData);

			HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expressionExe, null, input.getData(), null, runtimeEnvironment.getResourceManager());
			HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

			System.out.println(out);

//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
		catch(Throwable e) {
			e.printStackTrace();
//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
	}
}
