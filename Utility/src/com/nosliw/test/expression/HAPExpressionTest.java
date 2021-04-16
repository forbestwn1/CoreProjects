package com.nosliw.test.expression;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPUrlUtility;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;

public class HAPExpressionTest {

	public static void main(String[] args) {

		try {
			String suite = "test1";
			String[] ids1 = {"test10", "test11", "test12", "test20", "test21", "test22", "test23", "test25", "test26"};
			String[] ids = {"test23"};
			String[] failure = {"test13", "test24"};
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			for(String id : ids) {
				HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
				
				HAPResourceDefinitionExpressionGroup expressionDef = runtimeEnvironment.getExpressionManager().getExpressionDefinition(resourceId, null);
				
				HAPExecutableExpressionGroup expressionExe = runtimeEnvironment.getExpressionManager().getExpression(resourceId, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));

				Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(expressionDef, testData);

				HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expressionExe, null, (Map)input, null, runtimeEnvironment.getResourceManager());
				HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

				System.out.println("--------------------   "+id+"    -------------------------");
				System.out.println(out);
				System.out.println();
			}
			

//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
		catch(Throwable e) {
			e.printStackTrace();
			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
	}
}
