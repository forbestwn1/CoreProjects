package com.nosliw.test.uipage;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtimeenv.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.domain.HAPExecutablePackage;

public class HAPTestUIDomain {

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, "test1");

		//process
		HAPExecutablePackage executablePackage = runtimeEnvironment.getDomainEntityExecutableManager().getExecutablePackage(resourceId);
		
	}
}
