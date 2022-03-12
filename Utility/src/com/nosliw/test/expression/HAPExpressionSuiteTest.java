package com.nosliw.test.expression;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceIdExpressionSuite;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPExpressionSuiteTest {

	public static void main(String[] args) {
		String suite = "test_temp";

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPDomainDefinitionEntity defDomain = new HAPDomainDefinitionEntity(new HAPGeneratorId());
		HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(new HAPResourceIdExpressionSuite(suite), defDomain);
		String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), defDomain);
		System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));
	}
	
	
}
