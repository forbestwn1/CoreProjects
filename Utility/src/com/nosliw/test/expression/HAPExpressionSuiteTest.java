package com.nosliw.test.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroupInSuite;
import com.nosliw.data.core.expression.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;

public class HAPExpressionSuiteTest {

	public static void main(String[] args) {

		try {
			String suite = "test_temp";
			String[] ids1 = {"test10", "test11", "test12", "test20", "test21", "test22", "test23", "test25", "test26"};
			String[] ids = {"test22"};
			String[] failure = {"test13", "test24"};
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			for(String id : ids) {
				HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
				
				HAPResourceDefinitionExpressionGroup expressionDef = runtimeEnvironment.getExpressionManager().getExpressionDefinition(resourceId, null);
				
				HAPExecutableExpressionGroupInSuite expressionExe = (HAPExecutableExpressionGroupInSuite)runtimeEnvironment.getExpressionManager().getExpression(resourceId, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null));
				System.out.println(expressionExe);

				Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(expressionDef, testData);
				Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(expressionExe.getStructureDefinition(), input);
				
				Map<String, Object> varInput = new LinkedHashMap<String, Object>();
				for(String varName : expressionExe.getVariablesInfo().getVariablesName()) {
					HAPComplexPath path = new HAPComplexPath(varName);
					Object varValue = inputById.get(path.getRootName());
					for(String pathSeg : path.getPathSegs()) {
						if(varValue!=null) {
							if(varValue instanceof JSONObject) {
								varValue = ((JSONObject)varValue).opt(pathSeg);
							}
							else varValue = null;
						}
					}
					if(varValue!=null)   varInput.put(varName, varValue);
				}
				
				HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(expressionExe, null, (Map)varInput, null, runtimeEnvironment.getResourceManager());
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
