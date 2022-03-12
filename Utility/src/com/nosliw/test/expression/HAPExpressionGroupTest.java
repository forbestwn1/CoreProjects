package com.nosliw.test.expression;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPExpressionGroupTest {

	public static void main(String[] args) {

		try {
			String suite = "test_temp";
			String[] ids1 = {"test10", "test11", "test12", "test20", "test21", "test22", "test23", "test24", "test25", "test26"};
			String[] ids = {"test10"};
			String[] failure = {"test24"};
			String testData = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			for(String id : ids) {
				HAPResourceId resourceId = HAPUtilityExpressionResource.buildResourceId(suite, id);
				
				HAPDomainDefinitionEntity defDomain = new HAPDomainDefinitionEntity(new HAPGeneratorId());
				HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(resourceId, defDomain);
				String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), defDomain);
				System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));
				
/*				
				//process resource
				HAPResultExecutableEntityInDomain expressionInDomain = runtimeEnvironment.getExpressionManager().getExecutableComplexEntity(resourceId);
				
				//
				HAPInfoEntityComplex entityInfo = expressionInDomain.getComplexEntityInfoByExecutableId();
				HAPExecutableExpressionGroup expresionExecutable = (HAPExecutableExpressionGroup)entityInfo.getExecutable();
				HAPDefinitionEntityComplex expressionDef = entityInfo.getDefinition();
				HAPDefinitionEntityComplexValueStructure valueStructureComplex = entityInfo.getValueStructureComplex();
				HAPDefinitionEntityContainerAttachment attachmentContainer = entityInfo.getAttachmentContainer();

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
*/			
			}

//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
		catch(Throwable e) {
			e.printStackTrace();
//			HAPUrlUtility.openUrlInBrowser("http://localhost:8082/nosliw/fileload.html?name="+HAPRuntimeEnvironment.id);
		}
	}
}
