package com.nosliw.data.core.domain.entity.test.complex.testcomplex1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.test.HAPPluginEntityDefinitionInDomainDynamic;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestComplex1 extends HAPPluginEntityDefinitionInDomainDynamic{

	public HAPPluginEntityDefinitionInDomainTestComplex1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, HAPDefinitionEntityTestComplex1.class, true, runtimeEnv);
	}
	
}
