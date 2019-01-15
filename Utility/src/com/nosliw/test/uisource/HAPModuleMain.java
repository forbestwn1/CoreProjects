package com.nosliw.test.uisource;

import java.io.FileNotFoundException;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUtilityUIResource;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPModuleMain {

	static public void main(String[] args) throws FileNotFoundException {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(
				new HAPUITagManager(), 
				runtimeEnvironment.getExpressionSuiteManager(), 
				runtimeEnvironment.getResourceManager(),
				runtimeEnvironment.getProcessManager(),
				runtimeEnvironment.getRuntime(), 
				HAPExpressionManager.dataTypeHelper
			);
		
//		HAPDefinitionModule module = HAPUtilityUIResource.getUIModuleDefinitionById("ModuleMySchoolResult", uiResourceMan.getModuleParser());
		
		uiResourceMan.getUIModule("ModuleMySchoolResult");
		
		
	}
	
}
