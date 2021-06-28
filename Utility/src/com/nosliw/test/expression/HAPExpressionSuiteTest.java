package com.nosliw.test.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.value.HAPJsonValueUtility;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroupInSuite;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskExpression;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;

public class HAPExpressionSuiteTest {

	public static void main(String[] args) {

		try {
			String suite = "test1";
			String[] ids1 = {"test10", "test11", "test12", "test20", "test21", "test22", "test23", "test24", "test25", "test26"};
			String[] ids = {"test13"};
			String[] failure = {"test24"};
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			for(String id : ids) {
				HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
				
				HAPResourceDefinitionExpressionGroup expressionDef = runtimeEnvironment.getExpressionManager().getExpressionDefinition(resourceId, null);
				
				HAPExecutableExpressionGroupInSuite expressionExe = (HAPExecutableExpressionGroupInSuite)runtimeEnvironment.getExpressionManager().getExpression(resourceId, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));
				System.out.println(expressionExe);

				Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(expressionDef, testData);
				Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(expressionExe.getValueStructureDefinitionWrapper().getValueStructure(), input);
				
				Map<String, HAPData> varInput = new LinkedHashMap<String, HAPData>();
				for(String varName : expressionExe.getVariablesInfo().getVariablesName()) {
					Object varValue = HAPJsonValueUtility.getValue(inputById, new HAPComplexPath(varName));
					if(varValue!=null)   varInput.put(varName, HAPUtilityData.buildDataWrapperFromObject(varValue));					
				}
				
				HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(new HAPInfoRuntimeTaskExpression(expressionExe, null, varInput, null), runtimeEnvironment);
				HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);

				System.out.println("--------------------   "+id+"    -------------------------");
				System.out.println(out);
				System.out.println();
			}
			

//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
		catch(Throwable e) {
			e.printStackTrace();
//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
	}
}
