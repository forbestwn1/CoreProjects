package com.nosliw.test.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.value.HAPJsonValueUtility;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.complex.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.domain.HAPInfoEntityComplex;
import com.nosliw.data.core.domain.HAPResultExecutableEntityInDomain;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
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
			String[] ids = {"test10"};
			String[] failure = {"test24"};
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			for(String id : ids) {
				HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
				
				//process resource
				HAPResultExecutableEntityInDomain expressionInDomain = runtimeEnvironment.getExpressionManager().getExecutableComplexEntity(resourceId);
				
				//
				HAPInfoEntityComplex entityInfo = expressionInDomain.getComplexEntityInfoByExecutableId();
				HAPExecutableExpressionGroup expresionExecutable = (HAPExecutableExpressionGroup)entityInfo.getExecutable();
				HAPDefinitionEntityComplex expressionDef = entityInfo.getDefinition();
				HAPComplexValueStructure valueStructureComplex = entityInfo.getValueStructureComplex();
				HAPContainerAttachment attachmentContainer = entityInfo.getAttachmentContainer();

				//print out in json
				System.out.println(expresionExecutable.toString(expressionInDomain.getDomainContext().getExecutableDomain()));

				//input by name
				Map<String, Object> input = HAPUtilityAttachment.getTestValueFromAttachment(attachmentContainer, testData);
				//input by id
				Map<String, Object> inputById = HAPUtilityValueStructure.replaceValueNameWithId(valueStructureComplex, input);
				
				//prepare variable with input value
				Map<String, HAPData> varInput = new LinkedHashMap<String, HAPData>();
				for(String varName : expresionExecutable.getVariablesInfo().getVariablesId()) {
					Object varValue = HAPJsonValueUtility.getValue(inputById, new HAPComplexPath(varName));
					if(varValue!=null)   varInput.put(varName, HAPUtilityData.buildDataWrapperFromObject(varValue));					
				}
				
				//execute the expression
				HAPRuntimeTaskExecuteExpressionRhino task = new HAPRuntimeTaskExecuteExpressionRhino(new HAPInfoRuntimeTaskExpression(expresionExecutable, null, varInput, null), runtimeEnvironment);
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
