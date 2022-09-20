package com.nosliw.test.expression;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceIdExpressionSuite;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPExpressionSuiteTest {

	public static void main(String[] args) {
		String suite = "test_temp";

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPDomainEntityDefinitionGlobal globalDomain = new HAPDomainEntityDefinitionGlobal(new HAPGeneratorId(), runtimeEnvironment.getDomainEntityManager(), runtimeEnvironment.getResourceDefinitionManager());
		
		HAPResourceIdSimple resourceId = new HAPResourceIdExpressionSuite(suite);
		
		//definition
		HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(resourceId, globalDomain);
		String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), globalDomain);
		System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));

		//process
		HAPExecutablePackage executablePackage = runtimeEnvironment.getComplexEntityManager().getExecutablePackage(resourceId);
	}
	
	
}
