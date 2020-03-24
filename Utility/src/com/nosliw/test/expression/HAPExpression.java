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
		
		String suite = "test1";
		String id = "test4";
		String testData = "testData1";

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPExecutableExpression expressionExe = runtimeEnvironment.getExpressionManager().getExpression(HAPUtilityExpressionResource.buildResourceId(suite, id), null, HAPExpressionProcessConfigureUtil.setDoDiscovery(null));

		HAPContextDataFlat input = HAPUtilityComponent.getTestDataFromAttachment(expressionExe.getDefinition(), testData);

		HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expressionExe, input.getData(), null);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

		System.out.println(out);
	}
	
	
}
