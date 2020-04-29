package com.nosliw.test.expression;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;

public class HAPExpressionTest {

	public static void main(String[] args) {

		try {
			String suite = "test1";
			String id = "test6";
			String testData = "testData1";

			HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
			
			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			HAPResourceDefinitionExpressionGroup expressionDef = runtimeEnvironment.getExpressionManager().getExpressionDefinition(resourceId, null);
			
			HAPExecutableExpressionGroup expressionExe = runtimeEnvironment.getExpressionManager().getExpression(resourceId, null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));

			Map<String, Object> input = HAPAttachmentUtility.getTestValueFromAttachment(expressionDef, testData);

			HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expressionExe, null, (Map)input, null, runtimeEnvironment.getResourceManager());
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
