package com.nosliw.test.expression;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPResultExecutableEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceIdExpressionSuite;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPExpressionSuiteTest {

	public static void main(String[] args) {
		String suite = "test_temp";

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPResourceId resourceId = new HAPResourceIdExpressionSuite(suite);
		HAPDomainEntityDefinition defDomain = new HAPDomainEntityDefinition(new HAPGeneratorId(), runtimeEnvironment.getDomainEntityManager());
		HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(resourceId, defDomain);
		String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), defDomain);
		System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));
		
		//process entity
		HAPResultExecutableEntityInDomain expressionInDomain = runtimeEnvironment.getExpressionManager().getExecutableComplexEntity(resourceId);

	}
	
	
}
