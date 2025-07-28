package com.nosliw.test.uipage;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;

public class HAPTestUIDomain {

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, "test1");

		//process
		HAPExecutablePackage executablePackage = runtimeEnvironment.getDomainEntityExecutableManager().getExecutablePackage(resourceId);
		
	}
}
