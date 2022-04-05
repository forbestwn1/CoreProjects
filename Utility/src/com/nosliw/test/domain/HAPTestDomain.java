package com.nosliw.test.domain;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPResultExecutableEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPTestDomain {

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPDomainEntityDefinition defDomain = new HAPDomainEntityDefinition(new HAPGeneratorId(), runtimeEnvironment.getDomainEntityManager());

		HAPResourceId valueStructureResourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, "parent"); 

		HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(valueStructureResourceId, defDomain);
		String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), defDomain);
		System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));

		System.out.println();
		System.out.println();
		System.out.println("*******************************************************************************");
		System.out.println();
		System.out.println();
		HAPResultExecutableEntityInDomain executableResult = HAPUtilityDomain.getResourceExecutableComplexEntity(valueStructureResourceId, runtimeEnvironment);
		System.out.println(executableResult.getDomainContext().getExecutableDomain().toString());
	}
}
